package org.ffpy.myspringboot.sms.core;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.util.SpringContextUtils;

import java.util.Arrays;

public interface ISmsGroup {

    String getName();

    String getTemplateKey();

    default String getTemplateId() {
        String value = SpringContextUtils.getEnvironment().getProperty(getTemplateKey());
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("templateKey不能为空");
        }
        return value;
    }

    static <T extends ISmsGroup> T ofName(String name, Class<T> smsGroup) {
        return Arrays.stream(smsGroup.getEnumConstants())
                .filter(group -> group.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的类型"));
    }
}
