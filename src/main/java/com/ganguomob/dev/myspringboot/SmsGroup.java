package com.ganguomob.dev.myspringboot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.ganguomob.dev.myspringboot.sms.core.group.ISmsGroup;

@AllArgsConstructor
@Getter
public enum SmsGroup implements ISmsGroup {

    LOGIN(Names.LOGIN, "登录", "sms.template.login"),

    ;
    private final String name;
    private final String desc;
    private final String templateKey;

    public interface Names {
        String LOGIN = "login";
    }
}
