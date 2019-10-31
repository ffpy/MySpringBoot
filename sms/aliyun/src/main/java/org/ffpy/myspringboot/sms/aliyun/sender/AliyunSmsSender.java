package org.ffpy.myspringboot.sms.aliyun.sender;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.group.ISmsGroup;
import org.ffpy.myspringboot.sms.core.exception.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.sender.SmsSender;
import org.ffpy.myspringboot.sms.core.config.SmsProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/* 配置项:
# 阿里云AccessKey
aliyun.accessKey=
# 阿里云AccessSecret
aliyun.accessSecret=
# 短信签名
sms.signName=
# 短信模板验证码参数名
sms.template.param.code=
# 短信模板验证码过期时间参数名(分钟)
sms.template.param.expire=
*/

/**
 * 阿里云短信
 */
@Component
public class AliyunSmsSender implements SmsSender {

    /** 阿里云SMS相关 */
    private static final String REGION_ID = "ap-southeast-1";
    private static final String DOMAIN = "dysmsapi.ap-southeast-1.aliyuncs.com";
    private static final String VERSION = "2018-05-01";

    /** 发送国际短信动作 */
    private static final String ACTION_SEND_MESSAGE_TO_GLOBE = "SendMessageToGlobe";

    /** 发送模板短信动作 */
    private static final String ACTION_SEND_SMS_WITH_TEMPLATE = "SendMessageWithTemplate";

    @Value("${aliyun.accessKey.id}")
    private String accessKeyId;

    @Value("${aliyun.accessKey.secret}")
    private String accessKeySecret;

    @Value("${sms.signName}")
    private String signName;

    @Value("${sms.template.param.code:code}")
    private String paramCode;

    @Value("${sms.template.param.expire:}")
    private String paramExpire;

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void sendCode(String countryCode, String phone, ISmsGroup group, String code) throws SendSmsFailException {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("code不能为空");
        }

        Map<String, String> params = new HashMap<>(2);
        params.put(paramCode, code);
        if (StringUtils.isNotEmpty(paramExpire)) {
            params.put(paramExpire, Integer.toString(smsProperties.getExpire() / 60));
        }

        sendMessage(countryCode, phone, group, params);
    }

    @Override
    public void sendMessage(String countryCode, String phone, ISmsGroup group, Map<String, String> params) throws SendSmsFailException {
        checkCountryCode(countryCode);
        checkGroup(group);
        checkPhone(phone);
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("params不能为空");
        }

        new Sender(ACTION_SEND_SMS_WITH_TEMPLATE)
                .to(countryCode + phone)
                .from(signName)
                .param("TemplateCode", group.getTemplateId())
                .param("TemplateParam", new JSONObject(params).toString())
                .send();
    }

    @Override
    public void sendMessage(String countryCode, String phone, String message) throws SendSmsFailException {
        checkCountryCode(countryCode);
        checkPhone(phone);
        if (StringUtils.isEmpty(message)) {
            throw new IllegalArgumentException("message不能为空");
        }

        new Sender(ACTION_SEND_MESSAGE_TO_GLOBE)
                .to(countryCode + phone)
                .param("Message", message)
                .send();
    }

    private void checkPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            throw new IllegalArgumentException("phone不能为空");
        }
    }

    private void checkGroup(ISmsGroup group) {
        Objects.requireNonNull(group, "group不能为null");
    }

    private String checkCountryCode(String countryCode) {
        return Objects.requireNonNull(countryCode, "countryCode不能为null");
    }

    private class Sender {
        private final CommonRequest request = new CommonRequest();

        public Sender(String action) {
            request.setSysAction(Objects.requireNonNull(action));
        }

        public Sender to(String phone) {
            request.putQueryParameter("To", phone);
            return this;
        }

        public Sender from(String from) {
            request.putQueryParameter("From", from);
            return this;
        }

        public Sender param(String name, String value) {
            request.putQueryParameter(name, value);
            return this;
        }

        public void send() throws SendSmsFailException {
            try {
                DefaultProfile profile = DefaultProfile.getProfile(REGION_ID, accessKeyId, accessKeySecret);
                IAcsClient client = new DefaultAcsClient(profile);

                request.setSysMethod(MethodType.POST);
                request.setSysDomain(DOMAIN);
                request.setSysVersion(VERSION);

                CommonResponse response = client.getCommonResponse(request);
                if (response.getHttpStatus() != HttpStatus.OK.value()) {
                    try {
                        if (!"OK".equalsIgnoreCase(new JSONObject(response.getData()).getString("ResponseCode"))) {
                            throw new SendSmsFailException(response.getData());
                        }
                    } catch (JSONException e) {
                        throw new SendSmsFailException(response.getData(), e);
                    }
                }
            } catch (Exception e) {
                throw new SendSmsFailException(e);
            }
        }
    }
}
