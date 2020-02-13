package com.ganguomob.dev.myspringboot.sms.qcloud.sender;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.ganguomob.dev.myspringboot.sms.core.config.SmsProperties;
import com.ganguomob.dev.myspringboot.sms.core.exception.SendSmsFailException;
import com.ganguomob.dev.myspringboot.sms.core.group.ISmsGroup;
import com.ganguomob.dev.myspringboot.sms.core.sender.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 腾讯云短信发送器
 *
 * @author wenlongsheng
 */
@Component
public class QCloudSender implements SmsSender {

    private static final int RESULT_SUCCESS = 0;

    /** AppId */
    @Value("${sms.qcloud.appId}")
    private String appId;

    /** AppKey */
    @Value("${sms.qcloud.appKey}")
    private String appKey;

    /** 短信签名 */
    @Value("${sms.signName}")
    private String signName;

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void sendCode(String countryCode, String phone, ISmsGroup group, String code) throws SendSmsFailException {
        ArrayList<String> list = new ArrayList<>(2);
        list.add(code);
        list.add(smsProperties.getExpireMinute());
        doSendTemplate(countryCode, phone, group, list);
    }

    @Override
    public void sendTemplate(String countryCode, String phone, ISmsGroup group, LinkedHashMap<String, String> params) throws SendSmsFailException {
        ArrayList<String> paramList = new ArrayList<>(params.values());
        doSendTemplate(countryCode, phone, group, paramList);
    }

    @Override
    public void sendMessage(String countryCode, String phone, String message) throws SendSmsFailException {
        throw new UnsupportedOperationException();
    }

    private void doSendTemplate(String countryCode, String phone, ISmsGroup group, ArrayList<String> paramList) throws SendSmsFailException {
        SmsSingleSender sender = new SmsSingleSender(Integer.parseInt(appId), appKey);
        try {
            SmsSingleSenderResult result = sender.sendWithParam(countryCode, phone,
                    Integer.parseInt(group.getTemplateId()), paramList, signName, "", "");
            if (result.result != RESULT_SUCCESS) {
                throw new SendSmsFailException(result.toString());
            }
        } catch (HTTPException | IOException e) {
            throw new SendSmsFailException(e);
        }
    }
}
