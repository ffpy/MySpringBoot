package com.ganguomob.dev.myspringboot.cachehelper;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@ToString
abstract class InnerKey<T> {
    private static final int MAX_KEY_LENGTH = 2 * 1024;

    /** 键 */
    protected final Key key;

    /** 标签属性集合 */
    protected final Set<FullKey.Property> propertySet = new TreeSet<>();

    /** 是否有全局属性 */
    private boolean hasGlobalProperty = false;

    /**
     * 替换掉影响定位的字符
     */
    static String formatProperty(Object property) {
        String s = String.valueOf(property);
        if (s.isEmpty()) {
            return s;
        }
        for (String[] keyword : CacheHelper.KEYWORDS) {
            s = s.replace(keyword[0], keyword[1]);
        }
        return s;
    }

    protected InnerKey(InnerKey<?> fullKey) {
        this.key = fullKey.key;
        this.propertySet.addAll(fullKey.propertySet);
        this.hasGlobalProperty = fullKey.hasGlobalProperty;
    }

    /**
     * 设置依赖标签的指定属性
     *
     * @param tag      标签
     * @param property 属性
     * @return this
     */
    public T tag(Tag tag, Object property) {
        return addProperty(tag, new FullKey.Property(tag, property, false));
    }

    /**
     * 设置额外标签，属性为全局属性。
     * 注意，必须要能够不需要此标签属性也能唯一定位值，否则可能报多值错误。
     *
     * @param tag 标签
     * @return this
     */
    public T extraTag(Tag tag) {
        hasGlobalProperty = true;
        return addProperty(tag, new FullKey.Property(tag, CacheHelper.GLOBAL_PROPERTY, true));
    }

    /**
     * 设置额外列表标签。
     * 注意，必须要能够不需要此标签属性也能唯一定位值，否则可能报多值错误。
     *
     * @param tag      标签
     * @param property 属性
     * @return this
     */
    public <P extends Comparable<P>> T extraTag(Tag tag, P property) {
        return setExtraTag(tag, Stream.of(property));
    }

    /**
     * 设置额外列表标签。
     * 注意，必须要能够不需要此标签属性也能唯一定位值，否则可能报多值错误。
     *
     * @param tag        标签
     * @param properties 属性列表，会执行去重操作
     * @return this
     */
    public T extraTag(Tag tag, Collection<? extends Comparable<?>> properties) {
        return setExtraTag(tag, properties.stream());
    }

    /**
     * 设置额外列表标签。
     * 注意，必须要能够不需要此标签属性也能唯一定位值，否则可能报多值错误。
     *
     * @param tag            标签
     * @param propertyStream 属性流
     * @return this
     */
    public T extraTag(Tag tag, Stream<? extends Comparable<?>> propertyStream) {
        return setExtraTag(tag, propertyStream);
    }

    /**
     * 设置额外列表标签。
     * 注意，必须要能够不需要此标签属性也能唯一定位值，否则可能报多值错误。
     *
     * @param tag            标签
     * @param list           元素列表，会执行去重操作
     * @param propertyMapper 取元素的属性的函数
     * @return this
     */
    public <E, V extends Comparable<V>> T extraTag(Tag tag, Collection<E> list, Function<E, V> propertyMapper) {
        return setExtraTag(tag, list.stream().map(propertyMapper));
    }

    @SuppressWarnings("unchecked")
    protected T addProperty(Tag tag, FullKey.Property property) {
        if (tag != Tag.PAGE_TAG && !key.getTags().contains(tag)) {
            throw new IllegalArgumentException(key + "不关联" + tag);
        }

        if (!propertySet.add(property)) {
            // 更新property值
            propertySet.remove(property);
            propertySet.add(property);
        }
        return (T) this;
    }

    /**
     * 获取map键
     * 使用前调用{@link #check()}方法检查
     *
     * @return 键
     */
    protected String getMapKey() {
        // {tag(property)}
        return propertySet.stream().map(property ->
                "{" + property.getTag().getName() + "(" + property.getProperty() + ")}")
                .collect(Collectors.joining());
    }

    /**
     * 获取map键匹配字符串，在查询并且有全局属性时使用
     * 使用前调用{@link #check()}方法检查
     *
     * @return 匹配字符串
     */
    protected String getMapKeyPattern() {
        // *{tag(property)}{tag(*)}*
        return propertySet.stream().map(property ->
                "{" + property.getTag().getName() + "(" +
                        (property.isGlobal() ? "*" : property.getProperty())
                        + ")}")
                .collect(Collectors.joining("", "*", "*"));
    }

    /**
     * 是否有全局属性
     *
     * @return true为有，false为没有
     */
    protected boolean hasGlobalProperty() {
        return hasGlobalProperty;
    }

    /**
     * 检查标签是否正确
     */
    protected void check() {
        Set<Tag> tagSet = propertySet.stream().map(FullKey.Property::getTag).collect(Collectors.toSet());
        if (!tagSet.containsAll(key.getTags())) {
            throw new IllegalArgumentException("必须设置所有关联的标签");
        }
    }

    private T setExtraTag(Tag tag, Stream<? extends Comparable<?>> propertyStream) {
        hasGlobalProperty = true;

        String value = propertyStream.distinct()
                .sorted()
                .map(property -> "{" + tag.getName() + "(" + formatProperty(property) + ")}")
                .collect(Collectors.joining());

        if (value.length() > MAX_KEY_LENGTH) {
            return extraTag(tag);
        } else {
            return addProperty(tag, new FullKey.Property(tag, value, true, false));
        }
    }
}
