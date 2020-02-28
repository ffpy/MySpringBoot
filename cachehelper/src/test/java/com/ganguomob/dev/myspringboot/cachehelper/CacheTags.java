package com.ganguomob.dev.myspringboot.cachehelper;

/**
 * @author wenlongsheng
 * @date 2019/12/10
 */
public enum CacheTags {
    TEST_TAG1,
    TEST_TAG2,
    TEST_TAG3,

    ;
    private final Tag tag;

    CacheTags() {
        tag = new Tag(name().toLowerCase());
    }

    public Tag get() {
        return tag;
    }
}
