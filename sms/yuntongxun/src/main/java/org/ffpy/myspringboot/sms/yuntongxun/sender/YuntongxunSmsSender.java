package org.ffpy.myspringboot.sms.yuntongxun.sender;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.config.SmsProperties;
import org.ffpy.myspringboot.sms.core.exception.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.group.ISmsGroup;
import org.ffpy.myspringboot.sms.core.sender.SmsSender;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/* 配置项:
sms.sid=
sms.token=
sms.appId=
sms.baseUrl=
 */

/**
 * 容联云通讯
 */
@Component
public class YuntongxunSmsSender implements SmsSender {

    @Autowired
    private SmsProperties smsProperties;

    @Value("${sms.baseUrl:https://app.cloopen.com:8883}")
    private String baseUrl;

    @Value("${sms.sid}")
    private String sid;

    @Value("${sms.token}")
    private String token;

    @Value("${sms.appId}")
    private String appId;

    @Override
    public void sendCode(String countryCode, String phone, ISmsGroup group, String code) throws SendSmsFailException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = formatter.format(LocalDateTime.now());

        Map<String, Object> params = new HashMap<>(8);
        params.put("to", countryCode + phone);
        params.put("appId", appId);
        params.put("templateId", group.getTemplateId());
        params.put("datas", Arrays.asList(code, smsProperties.getExpireMinute()));

        HttpResponse response = null;
        try {
            response = HttpUtil.createPost(getUrl(timestamp))
                    .header("Accept", "application/json")
                    .header("Authorization", getAuthorization(timestamp))
                    .body(new cn.hutool.json.JSONObject(params).toString())
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
    public void sendTemplate(String countryCode, String phone, ISmsGroup group, LinkedHashMap<String, String> params) throws SendSmsFailException {

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
