package com.ganguomob.dev.myspringboot.sms.core.code;

/**
 * 验证码生成器
 *
 * @author wenlongsheng
 */
public interface CodeGenerator {

    /**
     * 生成指定长度的验证码
     *
     * @param length 长度
     * @return 验证码
     */
    String generate(int length);
}
