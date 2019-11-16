package org.ffpy.myspringboot.sms.core.code;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 字母验证码生成器
 *
 * @author wenlongsheng
 */
@Component
@ConditionalOnProperty(value = "sms.code.generator", havingValue = "alpha")
public class AlphaCodeGenerator extends AbstractCodeGenerator {

    public AlphaCodeGenerator() {
        super("ABCDEFGHIJKLMNPOQRSTUVWXYZ".toCharArray());
    }
}
