package org.ffpy.myspringboot.sms.core.store;

import org.ffpy.myspringboot.sms.core.SmsGroup;

public interface SmsStore {

    /**
     * 保存短信验证码
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @param code        验证码
     */
    void saveCode(SmsGroup group, String countryCode, String phone, String code);

    /**
     * 读取短信验证码
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @return 验证码
     */
    String getCode(SmsGroup group, String countryCode, String phone);

    /**
     * 移除短信验证码
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     */
    void removeCode(SmsGroup group, String countryCode, String phone);

    /**
     * 添加再次发送限制
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     */
    void addRepeatLimit(SmsGroup group, String countryCode, String phone);

    /**
     * 判断是不是处于再次发送限制状态中
     *
     * @param group       分组
     * @param countryCode 区号
     * @param phone       手机号
     * @return true为在限制状态中，false为不在限制状态中
     */
    boolean isInRepeatLimit(SmsGroup group, String countryCode, String phone);
}
