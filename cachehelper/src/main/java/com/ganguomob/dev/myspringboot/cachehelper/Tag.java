package com.ganguomob.dev.myspringboot.cachehelper;

import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author wenlongsheng
 */
@Getter
public class Tag implements Comparable<Tag> {
    private static final Set<String> NAME_SET = new HashSet<>();
    static final Tag PAGE_TAG = new Tag("_page_");

    private final String name;

    public Tag(String name) {
        this.name = checkName(name);
    }

    private String checkName(String name) {
        Objects.requireNonNull(name);

        // 检查是否已存在
        if (!NAME_SET.add(name)) {
            throw new IllegalArgumentException("Tag已存在");
        }

        // 检查格式是否正确
        if (!CacheHelper.NAME_PREDICATE.test(Objects.requireNonNull(name))) {
            throw new IllegalArgumentException("格式不正确");
        }

        // 检查是否包含关键字符
        for (String[] keyword : CacheHelper.KEYWORDS) {
            if (name.contains(keyword[0])) {
                throw new IllegalArgumentException("Tag " + name + "包含关键字符：" + keyword[0]);
            }
        }

        return name;
    }

    @Override
    public int compareTo(Tag o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
