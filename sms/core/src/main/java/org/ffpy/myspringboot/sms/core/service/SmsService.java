package org.ffpy.myspringboot.sms.core.service;

import org.ffpy.myspringboot.sms.core.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.SmsGroup;

public interface SmsService {

    /**
     * 发送短信验证码到指定手机号，验证码自动生成
     *
     * @param group 分组
     * @param phone 手机号
     * @return 发送的验证码
     */
    default String sendCode(SmsGroup group, String phone) throws SendSmsFailException {
        return sendCode(group, "", phone);
    }

    /**
     * 发送短信验证码到指定手机号，验证码自动生成
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @return 发送的验证码
     */
    String sendCode(SmsGroup group, String countryCode, String phone) throws SendSmsFailException;

    /**
     * 发送短信验证码到指定手机号
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @param code        验证码
     * @return 发送的验证码
     */
    String sendCode(SmsGroup group, String countryCode, String phone, String code) throws SendSmsFailException;

    /**
     * 获取指定手机号指定分组的验证码
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @return 验证码
     */
    String getCode(SmsGroup group, String countryCode, String phone);

    /**
     * 移除指定手机号指定分组的验证码
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     */
    void removeCode(SmsGroup group, String countryCode, String phone);
}
