package org.ffpy.myspringboot.sms.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SmsGroup implements ISmsGroup {

    /** 登录 */
    LOGIN("login", "sms.template.login"),

    ;
    private final String name;
    private final String templateKey;
}
