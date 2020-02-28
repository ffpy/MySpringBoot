package com.ganguomob.dev.myspringboot.cachehelper;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wenlongsheng
 */
public class Key {

    private static final Set<String> NAME_SET = new HashSet<>();

    /** 分级字符串，会替换为冒号 */
    private static final String DIR_SEP = "__";

    private final String name;
    private final String key;
    @Getter
    private final Set<Tag> tags;

    public Key(String name, Tag... tags) {
        List<Tag> tagList = Arrays.asList(tags);
        this.name = checkName(name);
        this.key = this.name + "#" + tagList.stream()
                .map(tag -> "{" + tag.getName() + "}")
                .collect(Collectors.joining());
        this.tags = tags.length == 0 ? Collections.emptySet() :
                Collections.unmodifiableSet(getTagSet(tagList));
    }

    public String getKey(String namespace) {
        // yejyapp:thread_title_and_created_at#{thread}{user}
        return Objects.requireNonNull(namespace) + ":" + key;
    }

    private String checkName(String name) {
        // 检查是否已存在
        if (!NAME_SET.add(name)) {
            throw new IllegalArgumentException("Key已存在");
        }

        // 检查格式是否正确
        if (!CacheHelper.NAME_PREDICATE.test(Objects.requireNonNull(name))) {
            throw new IllegalArgumentException("格式不正确");
        }

        return name.replace(DIR_SEP, ":");
    }

    private HashSet<Tag> getTagSet(List<Tag> tagList) {
        HashSet<Tag> tagSet = new HashSet<>(tagList.size());
        tagList.forEach(tag -> {
            if (!tagSet.add(tag)) {
                throw new IllegalArgumentException("发现重复Tag：" + tag);
            }
        });
        return tagSet;
    }

    @Override
    public String toString() {
        return name;
    }
}
