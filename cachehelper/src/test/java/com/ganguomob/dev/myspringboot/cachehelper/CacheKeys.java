package com.ganguomob.dev.myspringboot.cachehelper;

import java.util.Arrays;

import static com.ganguomob.dev.myspringboot.cachehelper.CacheTags.*;

/**
 * @author wenlongsheng
 * @date 2019/12/10
 */
public enum CacheKeys {
    TEST(TEST_TAG1, TEST_TAG2),
    TEST2(TEST_TAG1, TEST_TAG2, TEST_TAG3),
    TEST3(TEST_TAG1),

    ;
    private final Key key;

    CacheKeys(CacheTags... tags) {
        this.key = new Key(name().toLowerCase(), Arrays.stream(tags)
                .map(CacheTags::get).toArray(Tag[]::new));
    }

    public Key get() {
        return key;
    }
}
