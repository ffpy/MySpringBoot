package org.ffpy.myspringboot.sms.core.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/* 配置属性:
# 是否开启调试模式，调试模式不发送验证码，并且验证码固定为调试的验证码
sms.debug=
# 调试模式的验证码
sms.debug-code=
# 验证码过期时间(秒)，推荐为60的倍数
sms.expire=
# 允许再次发送时间间隔(秒)
sms.repeat-limit=
# 验证码长度
sms.code.length=
 */
@Component
@Getter
@Setter
@ToString
@Validated
public class SmsProperties {

    /** 是否开启调试模式 */
    @Value("${sms.debug:false}")
    private boolean debug;

    /** 调试模式的验证码 */
    @Value("${sms.debug-code:123456}")
    private String debugCode;

    /** 验证码过期时间(秒) */
    @Value("${sms.expire:300}")
    @Range(min = 0, max = 7200)
    private int expire;

    /** 允许再次发送时间间隔(秒) */
    @Value("${sms.repeat-limit:60}")
    @Range(min = 0, max = 3600)
    private int repeatLimit;

    /** 验证码长度 */
    @Value("${sms.code.length:6}")
    @Range(min = 0, max = 10)
    private int length;

    public String getExpireMinute() {
        return Integer.toString(expire / 60);
    }
}
