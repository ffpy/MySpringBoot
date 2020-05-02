package org.ffpy.myspringboot.common.util;

import java.util.Spliterator;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 流工具类
 */
public class StreamUtils {

    /**
     * 把Supplier转为流，返回null代表流结束
     *
     * @param supplier 数据提供器
     * @param <T>      数据元素类型
     * @return 流
     */
    public static <T> Stream<T> of(Supplier<T> supplier) {
        Spliterator<T> spliterator = new Spliterator<T>() {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                T e = supplier.get();
                if (e != null) {
                    action.accept(e);
                }
                return e != null;
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return -1;
            }

            @Override
            public int characteristics() {
                return ORDERED | IMMUTABLE | NONNULL;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    /**
     * 抛出UnsupportedOperationException异常
     */
    public static <T> BinaryOperator<T> getUnsupportedOperator() {
        return (t, t2) -> {
            throw new UnsupportedOperationException();
        };
    }
}
