## 缓存模块

### 使用步骤
1. 注入RedissonClient实例Bean
2. 在配置文件中添加属性
3. 创建CacheKeys和CacheTags枚举类
4. 注入CacheHelper依赖
5. 开始使用

### Properties
| 名称 | 说明 | 默认值 |
| ---- | ---- | ------ |
| cache.namespace | Redis键命名空间，即前缀 |
| cache.enable | 是否开启缓存 | true |
| cache.defaultTtl | 默认缓存过期时间（分钟） | 120 |
| cache.scanCount | Scan命令扫描的个数 | 1000 |

### CacheKeys
```java
public enum CacheKeys {
    TEST(TEST_TAG),

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
```

### CacheTags
```java
public enum CacheTags {
    TEST_TAG,

    ;
    private final Tag tag;

    CacheTags() {
        tag = new Tag(name().toLowerCase());
    }

    public Tag get() {
        return tag;
    }
}
```