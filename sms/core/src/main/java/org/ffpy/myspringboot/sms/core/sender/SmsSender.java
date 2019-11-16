package org.ffpy.myspringboot.sms.core.sender;

import org.ffpy.myspringboot.sms.core.exception.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.group.ISmsGroup;

import java.util.LinkedHashMap;

/**
 * 短信发送器
 *
 * @author wenlongsheng
 */
public interface SmsSender {

    /**
     * 发送短信验证码
     *
     * @param countryCode 国家区号
     * @param phone       手机号
     * @param group       分组
     * @param code        验证码
     * @throws SendSmsFailException 短信发送失败
     */
    void sendCode(String countryCode, String phone, ISmsGroup group, String code) throws SendSmsFailException;

    /**
     * 发送短信
     *
     * @param countryCode 国家区号
     * @param phone       手机号
     * @param group       分组
     * @param params      参数，用于填充短信模板，
     *                    用LinkedHashMap为是为保持顺序，有的短信接口的模板参数是按照位置填充的，如腾讯云
     * @throws SendSmsFailException 短信发送失败
     */
    void sendTemplate(String countryCode, String phone, ISmsGroup group, LinkedHashMap<String, String> params) throws SendSmsFailException;

    /**
     * 发送短信，不用短信模板，只能用在国际短信发送
     *
     * @param countryCode 国家区号
     * @param phone       手机号
     * @param message     内容
     * @throws SendSmsFailException 短信发送失败
     */
    void sendMessage(String countryCode, String phone, String message) throws SendSmsFailException;
}
