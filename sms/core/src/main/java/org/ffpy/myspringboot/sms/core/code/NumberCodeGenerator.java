package org.ffpy.myspringboot.sms.core.code;

import org.ffpy.myspringboot.common.util.RandomUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 数字验证码生成器
 *
 * @author wenlongsheng
 */
@Component
@ConditionalOnProperty(value = "sms.code.generator", havingValue = "number")
public class NumberCodeGenerator implements CodeGenerator {

    @Override
    public String generate(int length) {
        return RandomUtils.randomNumeric(length);
    }
}
