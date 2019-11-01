package org.ffpy.myspringboot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ffpy.myspringboot.sms.core.group.ISmsGroup;

@AllArgsConstructor
@Getter
public enum SmsGroup implements ISmsGroup {

    /** 登录 */
    LOGIN(Names.LOGIN, "sms.template.login"),

    ;

    private final String name;
    private final String templateKey;

    public interface Names {
        String LOGIN = "login";
    }
}
