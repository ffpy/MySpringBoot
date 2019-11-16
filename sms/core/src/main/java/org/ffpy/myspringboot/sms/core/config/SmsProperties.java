package org.ffpy.myspringboot.sms.core.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 短信模块配置属性
 *
 * @author wenlongsheng
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
    @Value("${sms.debugCode:123456}")
    @NotNull(message = "调试验证码不能为空")
    @NotBlank(message = "调试验证码不能为空")
    private String debugCode;

    /** 验证码过期时间(秒) */
    @Value("${sms.expire:300}")
    @Range(min = 0, max = 7200, message = "验证码过期时间必须位于区间[0, 7200]区间")
    private int expire;

    /** 允许再次发送时间间隔(秒) */
    @Value("${sms.repeatLimit:60}")
    @Range(min = 0, max = 3600, message = "允许再次发送时间必须位于[0,3600]区间")
    private int repeatLimit;

    /** 验证码长度 */
    @Value("${sms.code.length:6}")
    @Range(min = 0, max = 20, message = "验证码长度必须位于[0,20]区间")
    private int length;

    /** 默认的国家区号 */
    @Value("${sms.countryCode.default:86}")
    @NotNull(message = "默认国家区号不能为空")
    @NotBlank(message = "默认国家区号不能为空")
    private String defaultCountryCode;

    /** 支持的国家区号列表，格式[国家名称]_[国家区号]，如中国_86,澳洲_61，空值代表无限制 */
    @Value("${sms.countryCode.allows:}")
    private List<String> countryCodes;

    public String getExpireMinute() {
        return Integer.toString(expire / 60);
    }
}
