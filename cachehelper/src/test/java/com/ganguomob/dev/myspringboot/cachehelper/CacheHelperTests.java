package com.ganguomob.dev.myspringboot.cachehelper;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wenlongsheng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheHelperTests {

    @Autowired
    private CacheHelper cacheHelper;

    @Before
    public void setUp() throws Exception {
        cacheHelper.deleteAll();
    }

    @Test
    public void innerKeyTest() {
        FullKey key = new FullKey(CacheKeys.TEST2.get())
                .extraTag(CacheTags.TEST_TAG1.get(), Arrays.asList(1, 2, 3))
                .extraTag(CacheTags.TEST_TAG2.get(), Arrays.asList("a", "b", "c"))
                .tag(CacheTags.TEST_TAG3.get(), 1L);
        Assertions.assertThat(key.getMapKey()).isEqualTo("{test_tag1({test_tag1(1)}{test_tag1(2)}{test_tag1(3)})}{test_tag2({test_tag2(a)}{test_tag2(b)}{test_tag2(c)})}{test_tag3(1)}");
        Assertions.assertThat(key.getMapKeyPattern()).isEqualTo("*{test_tag1(*)}{test_tag2(*)}{test_tag3(1)}*");
    }

    @Test
    public void extraTagPropertiesTest() {
        FullKey simpleKey = new FullKey(CacheKeys.TEST2.get())
                .extraTag(CacheTags.TEST_TAG1.get())
                .extraTag(CacheTags.TEST_TAG2.get())
                .tag(CacheTags.TEST_TAG3.get(), 1L);
        FullKey fullKey = new FullKey(CacheKeys.TEST2.get())
                .extraTag(CacheTags.TEST_TAG1.get(), Arrays.asList(1, 2, 3))
                .extraTag(CacheTags.TEST_TAG2.get(), Arrays.asList("a", "b", "c"))
                .tag(CacheTags.TEST_TAG3.get(), 1L);
        String value = "1234";

        cacheHelper.put(fullKey, value);
        Assertions.assertThat((String) cacheHelper.get(simpleKey)).isEqualTo(value);
        Assertions.assertThat((String) cacheHelper.get(fullKey)).isEqualTo(value);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG1.get(), 10);
        Assertions.assertThat((String) cacheHelper.get(simpleKey)).isEqualTo(value);
        Assertions.assertThat((String) cacheHelper.get(fullKey)).isEqualTo(value);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG1.get(), 2);
        Assertions.assertThat((String) cacheHelper.get(simpleKey)).isNull();
        Assertions.assertThat((String) cacheHelper.get(fullKey)).isNull();
    }

    @Test
    public void specialPropertyTest() {
        FullKey key1 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG1.get(), "{test_tag1(1)}")
                .extraTag(CacheTags.TEST_TAG2.get());
        FullKey key2 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG1.get(), "1")
                .extraTag(CacheTags.TEST_TAG2.get());
        String value = "123";

        cacheHelper.put(key1, value);
        Assertions.assertThat((String) cacheHelper.get(key1)).isEqualTo(value);
        Assertions.assertThat((String) cacheHelper.get(key2)).isNull();

        cacheHelper.deleteByTag(CacheTags.TEST_TAG1.get(), "1");
        Assertions.assertThat((String) cacheHelper.get(key1)).isEqualTo(value);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG1.get(), "{test_tag1(1)}");
        Assertions.assertThat((String) cacheHelper.get(key1)).isNull();
    }

    @Test
    public void valueTest() {
        String value = "hello";
        cacheHelper.putValue(CacheKeys.TEST.get(), value);
        Assertions.assertThat((String) cacheHelper.getValue(CacheKeys.TEST.get())).isEqualTo(value);

        cacheHelper.deleteValueByTag(CacheTags.TEST_TAG1.get());
        Assertions.assertThat((String) cacheHelper.getValue(CacheKeys.TEST.get())).isNull();
    }

    @Test
    public void fullKeyTest() {
        FullKey fullKey1 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG1.get(), 1)
                .tag(CacheTags.TEST_TAG2.get(), 1);
        FullKey fullKey2 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG1.get(), 2)
                .tag(CacheTags.TEST_TAG2.get(), 1);

        String value1 = "value1";
        String value2 = "value2";

        cacheHelper.put(fullKey1, value1);
        cacheHelper.put(fullKey2, value2);

        Assertions.assertThat((String) cacheHelper.get(fullKey1)).isEqualTo(value1);
        Assertions.assertThat((String) cacheHelper.get(fullKey2)).isEqualTo(value2);

        cacheHelper.delete(fullKey1);

        Assertions.assertThat((String) cacheHelper.get(fullKey1)).isNull();
        Assertions.assertThat((String) cacheHelper.get(fullKey2)).isEqualTo(value2);
    }

    @Test
    public void helperTest() {
        String value1 = "value1";
        String value2 = "value2";

        cacheHelper.put(CacheKeys.TEST.get(), value1)
                .tag(CacheTags.TEST_TAG1.get(), 1)
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .execute();
        cacheHelper.put(CacheKeys.TEST.get(), value2)
                .tag(CacheTags.TEST_TAG1.get(), 2)
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .execute();

        Assertions.assertThat((String) cacheHelper.get(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG1.get(), 1)
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .execute()).isEqualTo(value1);
        Assertions.assertThat((String) cacheHelper.get(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG1.get(), 2)
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .execute()).isEqualTo(value2);
        Assertions.assertThat((String) cacheHelper.get(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG1.get(), 2)
                .tag(CacheTags.TEST_TAG2.get(), 3)
                .execute()).isNull();
    }

    @Test
    public void cacheValueTest() {
        final String value = "value";
        AtomicInteger cnt = new AtomicInteger(0);

        Assertions.assertThat(cacheHelper.cacheValue(CacheKeys.TEST.get(), () -> {
            cnt.incrementAndGet();
            return value;
        })).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        Assertions.assertThat(cacheHelper.cacheValue(CacheKeys.TEST.get(), () -> {
            cnt.incrementAndGet();
            return value;
        })).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);
    }

    @Test
    public void cacheValueNullTest() {
        AtomicInteger cnt = new AtomicInteger(0);

        Assertions.assertThat((String) cacheHelper.cacheValue(CacheKeys.TEST.get(), () -> {
            cnt.incrementAndGet();
            return null;
        })).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        Assertions.assertThat((String) cacheHelper.cacheValue(CacheKeys.TEST.get(), () -> {
            cnt.incrementAndGet();
            return null;
        })).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(2);
    }

    @Test
    public void cacheValueWithOptionalTest() {
        final String value = "value";
        AtomicInteger cnt = new AtomicInteger(0);

        Assertions.assertThat(cacheHelper.cacheValueWithOptional(CacheKeys.TEST.get(), () -> {
            cnt.incrementAndGet();
            return Optional.of(value);
        }).orElse(null)).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        Assertions.assertThat(cacheHelper.cacheValueWithOptional(CacheKeys.TEST.get(), () -> {
            cnt.incrementAndGet();
            return Optional.of(value);
        }).orElse(null)).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);
    }

    @Test
    public void cacheValueWithOptionalNullTest() {
        AtomicInteger cnt = new AtomicInteger(0);

        Assertions.assertThat(cacheHelper.cacheValueWithOptional(CacheKeys.TEST.get(), () -> {
            cnt.incrementAndGet();
            return Optional.empty();
        }).orElse(null)).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        Assertions.assertThat(cacheHelper.cacheValueWithOptional(CacheKeys.TEST.get(), () -> {
            cnt.incrementAndGet();
            return Optional.empty();
        }).orElse(null)).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(2);
    }

    @Test
    public void cacheTest() {
        final String value = "value";
        AtomicInteger cnt = new AtomicInteger(0);

        FullKey fullKey = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .extraTag(CacheTags.TEST_TAG1.get());

        Assertions.assertThat(cacheHelper.cache(fullKey, () -> {
            cnt.incrementAndGet();
            return value;
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2))).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        Assertions.assertThat(cacheHelper.cache(fullKey, () -> {
            cnt.incrementAndGet();
            return value;
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2))).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG2.get(), 1010);

        Assertions.assertThat(cacheHelper.cache(fullKey, () -> {
            cnt.incrementAndGet();
            return value;
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2))).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG1.get());

        Assertions.assertThat(cacheHelper.cache(fullKey, () -> {
            cnt.incrementAndGet();
            return value;
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2))).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(2);
    }

    @Test
    public void cacheNullTest() {
        AtomicInteger cnt = new AtomicInteger(0);

        FullKey fullKey = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .extraTag(CacheTags.TEST_TAG1.get());

        Assertions.assertThat((String) cacheHelper.cache(fullKey, () -> {
            cnt.incrementAndGet();
            return null;
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2))).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        Assertions.assertThat((String) cacheHelper.cache(fullKey, () -> {
            cnt.incrementAndGet();
            return null;
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2))).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(2);
    }

    @Test
    public void cacheWithOptionalTest() {
        final String value = "value";
        AtomicInteger cnt = new AtomicInteger(0);

        FullKey fullKey = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .extraTag(CacheTags.TEST_TAG1.get());

        Assertions.assertThat(cacheHelper.cacheWithOptional(fullKey, () -> {
            cnt.incrementAndGet();
            return Optional.of(value);
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2)).orElse(null)).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        Assertions.assertThat(cacheHelper.cacheWithOptional(fullKey, () -> {
            cnt.incrementAndGet();
            return Optional.of(value);
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2)).orElse(null)).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG2.get(), 1010);

        Assertions.assertThat(cacheHelper.cacheWithOptional(fullKey, () -> {
            cnt.incrementAndGet();
            return Optional.of(value);
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2)).orElse(null)).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG1.get(), 2);

        Assertions.assertThat(cacheHelper.cacheWithOptional(fullKey, () -> {
            cnt.incrementAndGet();
            return Optional.of(value);
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2)).orElse(null)).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(2);
    }

    @Test
    public void cacheWithOptionalNullTest() {
        AtomicInteger cnt = new AtomicInteger(0);

        FullKey fullKey = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .extraTag(CacheTags.TEST_TAG1.get());

        Assertions.assertThat(cacheHelper.cacheWithOptional(fullKey, () -> {
            cnt.incrementAndGet();
            return Optional.empty();
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2)).orElse(null)).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(1);

        Assertions.assertThat(cacheHelper.cacheWithOptional(fullKey, () -> {
            cnt.incrementAndGet();
            return Optional.empty();
        }, (k, v) -> k.tag(CacheTags.TEST_TAG1.get(), 2)).orElse(null)).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(2);
    }

    @Test
    public void deleteByRedisKeyTest() {
        FullKey fullKey1 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .tag(CacheTags.TEST_TAG1.get(), 2);
        FullKey fullKey2 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 2)
                .tag(CacheTags.TEST_TAG1.get(), 2);

        cacheHelper.put(fullKey1, "123");
        cacheHelper.put(fullKey2, "456");

        cacheHelper.delete(CacheKeys.TEST.get());

        Assertions.assertThat((String) cacheHelper.get(fullKey1)).isNull();
        Assertions.assertThat((String) cacheHelper.get(fullKey2)).isNull();
    }

    @Test
    public void deleteByFullKeyTest() {
        FullKey fullKey1 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .tag(CacheTags.TEST_TAG1.get(), 2);
        FullKey fullKey2 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 2)
                .tag(CacheTags.TEST_TAG1.get(), 2);

        String value1 = "123";
        String value2 = "456";

        cacheHelper.put(fullKey1, value1);
        cacheHelper.put(fullKey2, value2);

        cacheHelper.delete(fullKey1);

        Assertions.assertThat((String) cacheHelper.get(fullKey1)).isNull();
        Assertions.assertThat((String) cacheHelper.get(fullKey2)).isEqualTo(value2);
    }

    @Test
    public void deleteValueByTag() {
        cacheHelper.putValue(CacheKeys.TEST.get(), "123");
        cacheHelper.deleteValueByTag(CacheTags.TEST_TAG1.get());
        Assertions.assertThat((String) cacheHelper.getValue(CacheKeys.TEST.get())).isNull();
    }

    @Test
    public void deleteByTagTest() {
        FullKey fullKey1 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .tag(CacheTags.TEST_TAG1.get(), 2);
        FullKey fullKey2 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 2)
                .tag(CacheTags.TEST_TAG1.get(), 2);

        String value1 = "123";
        String value2 = "456";

        cacheHelper.put(fullKey1, value1);
        cacheHelper.put(fullKey2, value2);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG2.get(), 2);

        Assertions.assertThat((String) cacheHelper.get(fullKey1)).isNotNull();
        Assertions.assertThat((String) cacheHelper.get(fullKey2)).isNull();

        cacheHelper.deleteByTag(CacheTags.TEST_TAG2.get());

        Assertions.assertThat((String) cacheHelper.get(fullKey1)).isNull();
        Assertions.assertThat((String) cacheHelper.get(fullKey2)).isNull();
    }

    @Test
    public void deleteByTagAndPropertyTest() {
        FullKey fullKey1 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 1)
                .tag(CacheTags.TEST_TAG1.get(), 2);
        FullKey fullKey2 = new FullKey(CacheKeys.TEST.get())
                .tag(CacheTags.TEST_TAG2.get(), 2)
                .tag(CacheTags.TEST_TAG1.get(), 2);

        String value1 = "123";
        String value2 = "456";

        cacheHelper.put(fullKey1, value1);
        cacheHelper.put(fullKey2, value2);

        cacheHelper.deleteByTag(CacheTags.TEST_TAG2.get(), 2);

        Assertions.assertThat((String) cacheHelper.get(fullKey1)).isEqualTo(value1);
        Assertions.assertThat((String) cacheHelper.get(fullKey2)).isNull();
    }

    @Test(expected = NullPointerException.class)
    public void newKeyNullTest() {
        new Key(null);
    }

    @Test(expected = NullPointerException.class)
    public void newTagNullTest() {
        new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newKeyEmptyTest() {
        new Key("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void newTagEmptyTest() {
        new Tag("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void newKeyIncorrectFormatTest() {
        new Key("sdfjo2*");
    }

    @Test(expected = IllegalArgumentException.class)
    public void newTagIncorrectFormatTest() {
        new Tag("sfosf*");
    }

    @Test
    public void putPageTest() {
        Pageable pageable1 = PageRequest.of(2, 10);
        Pageable pageable2 = PageRequest.of(3, 10);
        Pageable pageable3 = PageRequest.of(4, 10);

        FullKey key1 = new FullKey(CacheKeys.TEST3.get())
                .tag(CacheTags.TEST_TAG1.get(), 1);
        FullKey key2 = new FullKey(CacheKeys.TEST3.get())
                .tag(CacheTags.TEST_TAG1.get(), 2);

        Page<String> value1 = new PageImpl<>(Arrays.asList("1", "2"), pageable1, 100);
        Page<String> value2 = new PageImpl<>(Arrays.asList("3", "4"), pageable2, 100);

        cacheHelper.putPage(key1, value1);
        cacheHelper.putPage(key1, value2);

        Assertions.assertThat(cacheHelper.getPage(key1, pageable1)).isEqualTo(value1);
        Assertions.assertThat(cacheHelper.getPage(key1, pageable2)).isEqualTo(value2);
        Assertions.assertThat(cacheHelper.getPage(key1, pageable3)).isNull();
        Assertions.assertThat(cacheHelper.getPage(key2, pageable1)).isNull();

        cacheHelper.deletePage(key2, pageable1);
        Assertions.assertThat(cacheHelper.getPage(key1, pageable1)).isEqualTo(value1);
        Assertions.assertThat(cacheHelper.getPage(key1, pageable2)).isEqualTo(value2);

        cacheHelper.deletePage(key1, pageable1);
        Assertions.assertThat(cacheHelper.getPage(key1, pageable1)).isNull();
        Assertions.assertThat(cacheHelper.getPage(key1, pageable2)).isEqualTo(value2);

        cacheHelper.deletePage(key1, pageable2);
        Assertions.assertThat(cacheHelper.getPage(key1, pageable1)).isNull();
        Assertions.assertThat(cacheHelper.getPage(key1, pageable2)).isNull();
    }

    @Test
    public void cachePageTest() {
        Pageable pageable = PageRequest.of(2, 10);

        FullKey key = new FullKey(CacheKeys.TEST3.get())
                .tag(CacheTags.TEST_TAG1.get(), 1);

        Page<String> value = new PageImpl<>(Arrays.asList("1", "2"), pageable, 100);

        AtomicInteger cnt = new AtomicInteger(0);

        Assertions.assertThat(cacheHelper.cachePage(key, pageable, () -> {
            cnt.incrementAndGet();
            return value;
        })).isEqualTo(value);

        Assertions.assertThat(cacheHelper.cachePage(key, pageable, () -> {
            cnt.incrementAndGet();
            return value;
        })).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);
    }

    @Test
    public void cachePageNullTest() {
        Pageable pageable = PageRequest.of(2, 10);
        FullKey key = new FullKey(CacheKeys.TEST3.get())
                .tag(CacheTags.TEST_TAG1.get(), 1);
        AtomicInteger cnt = new AtomicInteger(0);

        Assertions.assertThat(cacheHelper.cachePage(key, pageable, () -> {
            cnt.incrementAndGet();
            return null;
        })).isNull();

        Assertions.assertThat(cacheHelper.cachePage(key, pageable, () -> {
            cnt.incrementAndGet();
            return null;
        })).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(2);
    }

    @Test
    public void cachePageWithOptionalTest() {
        Pageable pageable = PageRequest.of(2, 10);
        FullKey key = new FullKey(CacheKeys.TEST3.get())
                .tag(CacheTags.TEST_TAG1.get(), 1);
        Page<String> value = new PageImpl<>(Arrays.asList("1", "2"), pageable, 100);
        AtomicInteger cnt = new AtomicInteger(0);

        Assertions.assertThat(cacheHelper.cachePageWithOptional(key, pageable, () -> {
            cnt.incrementAndGet();
            return Optional.of(value);
        }).orElse(null)).isEqualTo(value);

        Assertions.assertThat(cacheHelper.cachePageWithOptional(key, pageable, () -> {
            cnt.incrementAndGet();
            return Optional.of(value);
        }).orElse(null)).isEqualTo(value);
        Assertions.assertThat(cnt.get()).isEqualTo(1);
    }

    @Test
    public void cachePageWithOptionalNullTest() {
        Pageable pageable = PageRequest.of(2, 10);
        FullKey key = new FullKey(CacheKeys.TEST3.get())
                .tag(CacheTags.TEST_TAG1.get(), 1);
        AtomicInteger cnt = new AtomicInteger(0);

        Assertions.assertThat(cacheHelper.cachePageWithOptional(key, pageable, () -> {
            cnt.incrementAndGet();
            return Optional.empty();
        }).orElse(null)).isNull();

        Assertions.assertThat(cacheHelper.cachePageWithOptional(key, pageable, () -> {
            cnt.incrementAndGet();
            return Optional.empty();
        }).orElse(null)).isNull();
        Assertions.assertThat(cnt.get()).isEqualTo(2);
    }
}