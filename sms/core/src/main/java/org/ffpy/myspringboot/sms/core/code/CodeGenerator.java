package org.ffpy.myspringboot.sms.core.code;

/**
 * 验证码生成器
 */
public interface CodeGenerator {

    String generate(int length);
}
