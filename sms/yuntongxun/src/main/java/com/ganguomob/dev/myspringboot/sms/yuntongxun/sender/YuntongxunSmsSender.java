package com.ganguomob.dev.myspringboot.sms.yuntongxun.sender;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import com.ganguomob.dev.myspringboot.sms.core.config.SmsProperties;
import com.ganguomob.dev.myspringboot.sms.core.exception.SendSmsFailException;
import com.ganguomob.dev.myspringboot.sms.core.group.ISmsGroup;
import com.ganguomob.dev.myspringboot.sms.core.sender.SmsSender;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 容联云通讯短信发送器
 *
 * @author wenlongsheng
 */
@Component
public class YuntongxunSmsSender implements SmsSender {

    /** 短信接口，一般不用改 */
    @Value("${sms.yuntongxun.baseUrl:https://app.cloopen.com:8883}")
    private String baseUrl;

    /** 容联Sid */
    @Value("${sms.yuntongxun.sid}")
    private String sid;

    /** 容联Token */
    @Value("${sms.yuntongxun.token}")
    private String token;

    /** 容联应用ID */
    @Value("${sms.yuntongxun.appId}")
    private String appId;

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void sendCode(String countryCode, String phone, ISmsGroup group, String code) throws SendSmsFailException {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("code", code);
        params.put("expire", smsProperties.getExpireMinute());

        sendTemplate(countryCode, phone, group, params);
    }

    @Override
    public void sendTemplate(String countryCode, String phone, ISmsGroup group, LinkedHashMap<String, String> params) throws SendSmsFailException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = formatter.format(LocalDateTime.now());

        Map<String, Object> map = new HashMap<>(8);
        map.put("to", countryCode + phone);
        map.put("appId", appId);
        map.put("templateId", group.getTemplateId());
        map.put("datas", params.values());

        HttpResponse response = null;
        try {
            response = HttpUtil.createPost(getUrl(timestamp))
                    .header("Accept", "application/json")
                    .header("Authorization", getAuthorization(timestamp))
                    .body(new cn.hutool.json.JSONObject(map).toString())
                    .execute();

            if (response.isOk()) {
                if (new JSONObject(response.body()).getString("statusCode").equals("000000")) {
                    return;
                }
            }
        } catch (Exception e) {
            throw new SendSmsFailException(String.valueOf(response), e);
        }
        throw new SendSmsFailException(String.valueOf(response));
    }

    @Override
    public void sendMessage(String countryCode, String phone, String message) throws SendSmsFailException {
        throw new UnsupportedOperationException();
    }

    private String getAuthorization(String timestamp) {
        return Base64.getEncoder().encodeToString(
                StringUtils.join(sid, ":", timestamp).getBytes(StandardCharsets.UTF_8));
    }

    private String getUrl(String timestamp) {
        String sig = DigestUtils.md5Hex(StringUtils.join(sid, token, timestamp)).toUpperCase();
        return baseUrl + "/2013-12-26/Accounts/" + sid + "/SMS/TemplateSMS?sig=" + sig;
    }
}
