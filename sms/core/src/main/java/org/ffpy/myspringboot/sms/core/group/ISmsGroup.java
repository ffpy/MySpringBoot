package org.ffpy.myspringboot.sms.core.group;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.common.util.SpringContextUtils;

import java.util.Arrays;

/**
 * 短信分组
 *
 * @author wenlongsheng
 */
public interface ISmsGroup {

    /**
     * 获取短信分组的名称
     */
    String getName();

    /**
     * 获取短信分组对应的模块ID的Key，读取properties
     */
    String getTemplateKey();

    /**
     * 获取短信分组对应的模块ID
     */
    default String getTemplateId() {
        String value = SpringContextUtils.getEnvironment().getProperty(getTemplateKey());
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("templateKey不能为空");
        }
        return value;
    }

    /**
     * 根据短信分组名称获取对应的枚举类
     *
     * @param name     分组名称
     * @param smsGroup 短信分组枚举类类对象
     * @return 对应的短信分组枚举类
     */
    static <T extends ISmsGroup> T ofName(String name, Class<T> smsGroup) {
        return Arrays.stream(smsGroup.getEnumConstants())
                .filter(group -> group.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的类型"));
    }
}
