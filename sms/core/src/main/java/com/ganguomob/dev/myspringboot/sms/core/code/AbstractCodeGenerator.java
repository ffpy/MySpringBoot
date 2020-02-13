package com.ganguomob.dev.myspringboot.sms.core.code;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.ganguomob.dev.myspringboot.common.util.RandomUtils;

/**
 * 抽象验证码生成器，用于继承
 *
 * @author wenlongsheng
 */
@RequiredArgsConstructor
public abstract class AbstractCodeGenerator implements CodeGenerator {

    /** 随机字符集 */
    @NonNull
    protected final char[] chars;

    @Override
    public String generate(int length) {
        return RandomUtils.random(length, chars);
    }
}
