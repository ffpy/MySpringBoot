package org.ffpy.myspringboot.sms.core;

/**
 * 短信发送者
 */
public interface SmsSender {

    /**
     * 发送短信验证码
     *
     * @param countryCode 区号
     * @param phone       手机号
     * @param group       分组
     * @param code        验证码
     * @throws SendSmsFailException 发送短信失败
     */
    void send(String countryCode, String phone, SmsGroup group, String code) throws SendSmsFailException;

    /**
     * 发送短信，不用短信模板，只能用在国际短信发送
     *
     * @param countryCode 区号
     * @param phone       手机号
     * @param message     内容
     * @throws SendSmsFailException
     */
    void send(String countryCode, String phone, String message) throws SendSmsFailException;
}
