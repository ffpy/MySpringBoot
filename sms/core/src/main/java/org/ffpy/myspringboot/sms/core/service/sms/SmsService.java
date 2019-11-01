package org.ffpy.myspringboot.sms.core.service.sms;

import org.ffpy.myspringboot.sms.core.exception.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.group.ISmsGroup;

public interface SmsService {

    /**
     * 发送短信验证码到指定手机号，验证码自动生成
     *
     * @param group 分组
     * @param phone 手机号
     * @return 发送的验证码
     */
    String sendCode(ISmsGroup group, String phone) throws SendSmsFailException;

    /**
     * 发送短信验证码到指定手机号，验证码自动生成
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @return 发送的验证码
     */
    String sendCode(ISmsGroup group, String countryCode, String phone) throws SendSmsFailException;

    /**
     * 发送短信验证码到指定手机号
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @param code        验证码
     * @return 发送的验证码
     */
    String sendCode(ISmsGroup group, String countryCode, String phone, String code) throws SendSmsFailException;

    /**
     * 获取指定手机号指定分组的验证码
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @return 验证码
     */
    String getCode(ISmsGroup group, String countryCode, String phone);

    /**
     * 移除指定手机号指定分组的验证码
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     */
    void removeCode(ISmsGroup group, String countryCode, String phone);
}
