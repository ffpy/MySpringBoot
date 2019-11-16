package org.ffpy.myspringboot.sms.core.code;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 字母+数字验证码生成器
 *
 * @author wenlongsheng
 */
@Component
@ConditionalOnProperty(value = "sms.code.generator", havingValue = "alpha-number")
public class AlphaNumberCodeGenerator extends AbstractCodeGenerator {

    public AlphaNumberCodeGenerator() {
        // 去掉容易混淆的字符
        super("23456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray());
    }
}
