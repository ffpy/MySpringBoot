package com.ganguomob.dev.myspringboot.cachehelper;

import lombok.ToString;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Redis缓存工具类
 * Github地址: https://github.com/ffpy/MySpringBoot/tree/ganguo/cachehelper
 *
 * @author wenlongsheng
 */
@Component
public class CacheHelper {

    /** 键名格式 */
    static final Predicate<String> NAME_PREDICATE = Pattern.compile("^[\\w_:]+$").asPredicate();

    /** 默认过期时间单位 */
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    /** 单值缓存的键名 */
    private static final String KEY_SINGLE_VALUE = "";

    /** 全局属性名 */
    static final String GLOBAL_PROPERTY = "";

    /** 关键词字符 */
    public static final String[][] KEYWORDS = {
            {"{", "{{"},
            {"}", "}}"},
            {"(", "(("},
            {")", "))"},
    };

    /** redis命名空间 */
    @Value("${cache.namespace}")
    private String namespace;

    /** 是否启用缓存 */
    @Value("${cache.enable:true}")
    private boolean cacheEnable;

    /** 默认过期时间(分钟) */
    @Value("${cache.defaultTtl:120}")
    private int defaultTtl;

    /** Scan命令扫描的个数 */
    @Value("${cache.scanCount:1000}")
    private int scanCount;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取命名空间
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * 获取单值缓存
     *
     * @param key 键
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(Key key) {
        if (!cacheEnable) {
            return null;
        }

        return (T) redissonClient.getMapCache(key.getKey(namespace)).get(KEY_SINGLE_VALUE);
    }

    /**
     * 获取缓存
     *
     * @param fullKey 键
     * @return RedisGetHelper
     */
    public <T> T get(FullKey fullKey) {
        return new RedisGetHelper(fullKey).execute();
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return RedisGetHelper
     */
    public RedisGetHelper get(Key key) {
        return new RedisGetHelper(key);
    }

    /**
     * 获取分页缓存
     *
     * @param fullKey  键
     * @param pageable 分页参数
     * @param <E>      元素类型
     * @return 值
     */
    public <E> Page<E> getPage(FullKey fullKey, Pageable pageable) {
        PageValue<E> pageValue = new RedisGetHelper(fullKey)
                .tag(Tag.PAGE_TAG, getPageableProperty(pageable))
                .execute();
        return pageValue == null ? null : pageValue.toPage();
    }

    /**
     * 添加单值缓存
     *
     * @param key   键
     * @param value 值
     */
    public void putValue(Key key, Object value) {
        if (!cacheEnable) {
            return;
        }

        // thread_title_and_created_at#{thread}{user}:{thread(1)}{user(2)}
        redissonClient.getMapCache(key.getKey(namespace))
                .fastPut(KEY_SINGLE_VALUE, value, defaultTtl, DEFAULT_TIME_UNIT);
    }


    /**
     * 添加缓存
     *
     * @param fullKey 键
     * @param value   值
     */
    public void put(FullKey fullKey, Object value) {
        // thread_title_and_created_at#{thread}{user}:{thread(1)}{user(2)}
        new RedisPutHelper(fullKey, value).execute();
    }

    /**
     * 添加缓存
     *
     * @param key   键
     * @param value 值
     * @return RedisPutHelper
     */
    public RedisPutHelper put(Key key, Object value) {
        // thread_title_and_created_at#{thread}{user}:{thread(1)}{user(2)}
        return new RedisPutHelper(key, value);
    }

    /**
     * 缓存分页对象
     *
     * @param fullKey 键
     * @param page    分页对象
     * @param <E>     元素类型
     */
    public <E> void putPage(FullKey fullKey, Page<E> page) {
        putPage(fullKey, page, page.getPageable());
    }

    /**
     * 缓存分页对象
     *
     * @param fullKey  键
     * @param page     分页对象
     * @param pageable 分页参数
     * @param <E>      元素类型
     */
    public <E> void putPage(FullKey fullKey, Page<E> page, Pageable pageable) {
        new RedisPutHelper(fullKey, new PageValue<>(page))
                .tag(Tag.PAGE_TAG, getPageableProperty(pageable))
                .execute();
    }

    /**
     * 使用缓存
     *
     * @param key      键
     * @param supplier 缓存不存在时提供值的函数
     * @return 值
     */
    public <V> V cacheValue(Key key, Supplier<V> supplier) {
        V value = getValue(key);
        if (value == null) {
            value = supplier.get();
            if (value != null) {
                putValue(key, value);
            }
        }
        return value;
    }

    /**
     * 使用缓存
     *
     * @param key      键
     * @param supplier 缓存不存在时提供值的函数
     * @return 值
     */
    public <V> Optional<V> cacheValueWithOptional(Key key, Supplier<Optional<V>> supplier) {
        V value = getValue(key);
        if (value != null) {
            return Optional.of(value);
        }

        Optional<V> result = supplier.get();
        result.ifPresent(v -> putValue(key, v));
        return result;
    }

    /**
     * 使用缓存
     *
     * @param fullKey  键
     * @param supplier 缓存不存在时提供值的函数
     * @return 值
     */
    public <V> V cache(FullKey fullKey, Supplier<V> supplier) {
        return cache(fullKey, supplier, (k, v) -> k);
    }

    /**
     * 使用缓存
     *
     * @param fullKey   键
     * @param supplier  缓存不存在时提供值的函数
     * @param keyMapper 放入缓存时的fullKey转换函数
     * @return 值
     */
    public <V> V cache(FullKey fullKey, Supplier<V> supplier, BiFunction<FullKey, V, FullKey> keyMapper) {
        V value = get(fullKey);
        if (value == null) {
            value = supplier.get();
            if (value != null) {
                put(keyMapper.apply(fullKey, value), value);
            }
        }
        return value;
    }

    /**
     * 使用缓存
     *
     * @param fullKey  键
     * @param supplier 缓存不存在时提供值的函数
     * @return 值
     */
    public <V> Optional<V> cacheWithOptional(FullKey fullKey, Supplier<Optional<V>> supplier) {
        return cacheWithOptional(fullKey, supplier, (k, v) -> k);
    }

    /**
     * 使用缓存
     *
     * @param fullKey   键
     * @param supplier  缓存不存在时提供值的函数
     * @param keyMapper 放入缓存时的fullKey转换函数
     * @return 值
     */
    public <V> Optional<V> cacheWithOptional(FullKey fullKey, Supplier<Optional<V>> supplier,
                                             BiFunction<FullKey, V, FullKey> keyMapper) {
        V value = get(fullKey);
        if (value != null) {
            return Optional.of(value);
        }

        Optional<V> result = supplier.get();
        result.ifPresent(v -> put(keyMapper.apply(fullKey, v), v));
        return result;
    }

    /**
     * 使用缓存
     *
     * @param fullKey  键
     * @param supplier 缓存不存在时提供值的函数
     * @return 值
     */
    public <E> Page<E> cachePage(
            FullKey fullKey, Pageable pageable, Supplier<Page<E>> supplier) {
        PageValue<E> pageValue = cache(
                new FullKey(fullKey).tag(Tag.PAGE_TAG, getPageableProperty(pageable)),
                getPageSupplier(supplier)
        );
        return pageValue == null ? null : pageValue.toPage();
    }

    /**
     * 使用缓存
     *
     * @param fullKey   键
     * @param supplier  缓存不存在时提供值的函数
     * @param keyMapper 放入缓存时的fullKey转换函数
     * @return 值
     */
    public <E> Page<E> cachePage(
            FullKey fullKey, Pageable pageable,
            Supplier<Page<E>> supplier, BiFunction<FullKey, Page<E>, FullKey> keyMapper) {
        PageValue<E> pageValue = cache(
                new FullKey(fullKey).tag(Tag.PAGE_TAG, getPageableProperty(pageable)),
                getPageSupplier(supplier), getPageKeyMapper(keyMapper)
        );
        return pageValue == null ? null : pageValue.toPage();
    }

    /**
     * 使用缓存
     *
     * @param fullKey  键
     * @param supplier 缓存不存在时提供值的函数
     * @return 值
     */
    public <E> Optional<Page<E>> cachePageWithOptional(
            FullKey fullKey, Pageable pageable, Supplier<Optional<Page<E>>> supplier) {
        return cacheWithOptional(new FullKey(fullKey)
                .tag(Tag.PAGE_TAG, getPageableProperty(pageable)), getPageSupplierWithOptional(supplier))
                .map(PageValue::toPage);
    }

    /**
     * 使用缓存
     *
     * @param fullKey   键
     * @param supplier  缓存不存在时提供值的函数
     * @param keyMapper 放入缓存时的fullKey转换函数
     * @return 值
     */
    public <E> Optional<Page<E>> cachePageWithOptional(
            FullKey fullKey, Pageable pageable,
            Supplier<Optional<Page<E>>> supplier, BiFunction<FullKey, Page<E>, FullKey> keyMapper) {
        Optional<PageValue<E>> pageValue = cacheWithOptional(
                new FullKey(fullKey).tag(Tag.PAGE_TAG, getPageableProperty(pageable)),
                getPageSupplierWithOptional(supplier),
                getPageKeyMapper(keyMapper)
        );
        return pageValue.map(PageValue::toPage);
    }

    /**
     * 删除缓存
     *
     * @param keys 键
     */
    public void delete(Key... keys) {
        if (!cacheEnable) {
            return;
        }

        redissonClient.getKeys().delete(
                Arrays.stream(keys).map(key -> key.getKey(namespace)).toArray(String[]::new));
    }

    /**
     * 删除缓存
     *
     * @param fullKeys 键
     */
    public void delete(FullKey... fullKeys) {
        if (!cacheEnable) {
            return;
        }

        for (FullKey fullKey : fullKeys) {
            fullKey.check();

            RMapCache<String, ?> map = redissonClient.getMapCache(fullKey.key.getKey(namespace));
            List<String> removeKeys = new LinkedList<>();
            if (fullKey.hasGlobalProperty()) {
                removeKeys.addAll(map.keySet(fullKey.getMapKeyPattern()));
            } else {
                removeKeys.add(fullKey.getMapKey());
            }

            removeKeys.add(KEY_SINGLE_VALUE);
            removeMapKeys(map, removeKeys);
        }
    }

    /**
     * 删除分页缓存
     *
     * @param fullKey  键
     * @param pageable 分页参数
     */
    public void deletePage(FullKey fullKey, Pageable pageable) {
        delete(new FullKey(fullKey)
                .tag(Tag.PAGE_TAG, getPageableProperty(pageable)));
    }

    /**
     * 删除指定标签的单值缓存
     *
     * @param tags 标签
     */
    public void deleteValueByTag(Tag... tags) {
        if (!cacheEnable) {
            return;
        }

        for (Tag tag : tags) {
            redissonClient.getKeys().getKeysByPattern(getPatternByTag(tag), scanCount).forEach(key -> {
                RMapCache<String, ?> map = redissonClient.getMapCache(key);
                map.fastRemove(KEY_SINGLE_VALUE);
            });
        }
    }

    /**
     * 删除指定标签的缓存
     *
     * @param tags 标签
     */
    public void deleteByTag(Tag... tags) {
        if (!cacheEnable) {
            return;
        }

        for (Tag tag : tags) {
            redissonClient.getKeys().deleteByPattern(getPatternByTag(tag));
        }

        deleteValueByTag(tags);
    }

    /**
     * 删除指定标签和属性的缓存
     *
     * @param tag      标签
     * @param property 属性
     */
    public void deleteByTag(Tag tag, Object property) {
        if (!cacheEnable || property == null) {
            return;
        }

        deleteByTag(tag, Collections.singletonList(property));
    }

    /**
     * 删除指定标签和属性的缓存
     *
     * @param tag            标签
     * @param propertyStream 属性流
     */
    public void deleteByTag(Tag tag, Stream<?> propertyStream) {
        if (!cacheEnable || propertyStream == null) {
            return;
        }

        deleteByTag(tag, propertyStream.collect(Collectors.toList()));
    }

    /**
     * 删除指定标签和属性的缓存
     *
     * @param tag        标签
     * @param properties 属性列表
     */
    public void deleteByTag(Tag tag, List<?> properties) {
        if (!cacheEnable || properties.isEmpty()) {
            return;
        }

        String pattern = getPatternByTag(tag);
        redissonClient.getKeys().getKeysByPattern(pattern, scanCount).forEach(key -> {
            RMapCache<String, ?> map = redissonClient.getMapCache(key);
            List<String> removeKeys = new LinkedList<>();
            properties.forEach(property -> {
                // 删除指定值属性
                removeKeys.addAll(map.keySet(getPatternByProperty(tag, InnerKey.formatProperty(property))));
            });
            // 删除全局属性
            removeKeys.addAll(map.keySet(getPatternByProperty(tag, GLOBAL_PROPERTY)));
            // 删除单值属性
            removeKeys.add(KEY_SINGLE_VALUE);
            removeMapKeys(map, removeKeys);
        });
    }

    /**
     * 删除命名空间下的所有键
     */
    public void deleteAll() {
        redissonClient.getKeys().deleteByPattern(namespace + ":" + "*#*");
    }

    /**
     * 判断单值键是否存在
     *
     * @param key 键
     * @return true为存在，false为不存在
     */
    public boolean existsValue(Key key) {
        RMapCache<String, ?> map = redissonClient.getMapCache(key.getKey(namespace));
        return map.containsKey(KEY_SINGLE_VALUE);
    }

    /**
     * 判断键是否存在
     *
     * @param fullKey 键
     * @return true为存在，false为不存在
     */
    public boolean exists(FullKey fullKey) {
        fullKey.check();
        RMapCache<String, ?> map = redissonClient.getMapCache(fullKey.key.getKey(namespace));
        if (fullKey.hasGlobalProperty()) {
            return !map.keySet(fullKey.getMapKeyPattern()).isEmpty();
        } else {
            return map.containsKey(fullKey.getMapKey());
        }
    }

    /**
     * 判断分页键是否存在
     *
     * @param fullKey  键
     * @param pageable 分页参数
     * @return true为存在，false为不存在
     */
    public boolean existsPage(FullKey fullKey, Pageable pageable) {
        return exists(new FullKey(fullKey)
                .tag(Tag.PAGE_TAG, getPageableProperty(pageable)));
    }

    /**
     * 获取标签匹配字符串
     *
     * @param tag 标签
     * @return 匹配字符串
     */
    private String getPatternByTag(Tag tag) {
        return namespace + ":" + "*#*{" + tag.getName() + "}*";
    }

    /**
     * 获取标签属性匹配字符串
     *
     * @param tag      标签
     * @param property 属性
     * @return 匹配字符串
     */
    private String getPatternByProperty(Tag tag, String property) {
        return "*{" + tag.getName() + "(" + property + ")}*";
    }

    /**
     * 获取分页参数属性
     *
     * @param pageable 分页参数
     * @return 属性值
     */
    private String getPageableProperty(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable不能为null");
        return pageable.getPageNumber() + "," +
                pageable.getPageSize() + "," +
                pageable.getSort().toString();
    }

    /**
     * 删除map中指定的键
     *
     * @param map  RMapCache
     * @param keys 键列表
     */
    private void removeMapKeys(RMapCache<String, ?> map, List<String> keys) {
        if (!keys.isEmpty()) {
            map.fastRemove(keys.toArray(new String[0]));
        }
    }

    private <E> Supplier<PageValue<E>> getPageSupplier(Supplier<Page<E>> supplier) {
        return () -> {
            Page<E> page = supplier.get();
            return page == null ? null : new PageValue<E>(page);
        };
    }

    private <E> Supplier<Optional<PageValue<E>>> getPageSupplierWithOptional(Supplier<Optional<Page<E>>> supplier) {
        return () -> supplier.get().map(PageValue::new);
    }

    private <E> BiFunction<FullKey, PageValue<E>, FullKey> getPageKeyMapper(
            BiFunction<FullKey, Page<E>, FullKey> keyMapper) {
        return (k, v) -> keyMapper.apply(k, v.toPage());
    }

    @ToString
    public class RedisPutHelper extends InnerKey<RedisPutHelper> {
        /** 缓存值 */
        private final Object value;

        /** 过期时间 */
        private long ttl = defaultTtl;

        private RedisPutHelper(Key key, Object value) {
            super(key);
            this.value = value;
        }

        private RedisPutHelper(FullKey fullKey, Object value) {
            super(fullKey);
            this.value = value;
        }

        /**
         * 设置过期时间(分钟)
         *
         * @param minute 过期时间
         * @return this
         */
        public RedisPutHelper expire(long minute) {
            if (minute < 0) {
                throw new IllegalArgumentException("过期时间必须大于等于0");
            }

            ttl = minute;
            return this;
        }

        /**
         * 放入缓存
         */
        public void execute() {
            check();

            if (!cacheEnable) {
                return;
            }

            redissonClient.getMapCache(key.getKey(namespace))
                    .fastPut(getMapKey(), value, ttl, DEFAULT_TIME_UNIT);
        }

    }

    @ToString
    public class RedisGetHelper extends InnerKey<RedisGetHelper> {

        private RedisGetHelper(Key key) {
            super(key);
        }

        private RedisGetHelper(FullKey fullKey) {
            super(fullKey);
        }

        /**
         * 取出缓存
         *
         * @return 缓存值，不存在则为null
         */
        @SuppressWarnings("unchecked")
        public <T> T execute() {
            check();

            if (!cacheEnable) {
                return null;
            }

            RMapCache<String, ?> map = redissonClient.getMapCache(key.getKey(namespace));

            if (hasGlobalProperty()) {
                String mapKeyPattern = getMapKeyPattern();
                Set<String> keys = map.keySet(mapKeyPattern);
                int size = keys.size();
                if (size == 0) {
                    return null;
                }
                if (size == 1) {
                    return (T) map.get(keys.iterator().next());
                }

                // 匹配到不止一个键，说明提供的键不满足唯一性
                throw new RuntimeException("找到多个值" +
                        key.getKey(namespace) + " => " + getMapKeyPattern());
            } else {
                return (T) map.get(getMapKey());
            }
        }
    }
}
