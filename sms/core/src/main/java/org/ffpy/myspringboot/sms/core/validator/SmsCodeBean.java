package org.ffpy.myspringboot.sms.core.validator;

/**
 * 用于{@link SmsCode}验证
 */
public interface SmsCodeBean {

    /**
     * 获取区号
     */
    String getCountryCode();

    /**
     * 获取手机号
     */
    String getPhone();

    /**
     * 获取验证码
     */
    String getCode();
}
