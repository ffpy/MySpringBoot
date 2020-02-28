package com.ganguomob.dev.myspringboot.cachehelper;

import lombok.Getter;

import java.util.Objects;

public class FullKey extends InnerKey<FullKey> {

    public FullKey(Key key) {
        super(key);
    }

    protected FullKey(FullKey fullKey) {
        super(fullKey);
    }

    @Getter
    static class Property implements Comparable<Property> {
        /** 标签 */
        private final Tag tag;

        /** 标签的属性 */
        private final String property;

        /** 是否为全局属性 */
        private final boolean isGlobal;

        public Property(Tag tag, Object property, boolean isGlobal) {
            this(tag, property, isGlobal, true);
        }

        public Property(Tag tag, Object property, boolean isGlobal, boolean formatProperty) {
            this.tag = Objects.requireNonNull(tag);
            this.property = formatProperty ? formatProperty(property) : String.valueOf(property);
            this.isGlobal = isGlobal;
        }

        /**
         * 只比较tag字段
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Property property = (Property) o;
            return tag == property.tag;
        }

        /**
         * 只比较tag字段
         */
        @Override
        public int hashCode() {
            return Objects.hash(tag);
        }

        @Override
        public int compareTo(Property o) {
            return this.tag.compareTo(o.tag);
        }
    }
}
