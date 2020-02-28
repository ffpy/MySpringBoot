package com.ganguomob.dev.myspringboot.cachehelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenlongsheng
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
class PageValue<E> {
    private List<E> content;
    private PageableValue pageable;
    private long total;

    public PageValue(Page<E> page) {
        this.content = page.getContent();
        this.pageable = new PageableValue(page.getPageable());
        this.total = page.getTotalElements();
    }

    public Page<E> toPage() {
        return new PageImpl<E>(content, pageable.toPageable(), total);
    }

    @NoArgsConstructor
    static class PageableValue {
        private int page;
        private int size;
        private SortValue sort;

        public PageableValue(Pageable pageable) {
            this.page = pageable.getPageNumber();
            this.size = pageable.getPageSize();
            this.sort = new SortValue(pageable.getSort());
        }

        public Pageable toPageable() {
            return PageRequest.of(page, size, sort.toSort());
        }
    }

    @NoArgsConstructor
    static class SortValue {
        private List<OrderValue> orders;

        public SortValue(Sort sort) {
            this.orders = sort.stream().map(OrderValue::new).collect(Collectors.toList());
        }

        public Sort toSort() {
            return Sort.by(orders.stream().map(OrderValue::toOrder).collect(Collectors.toList()));
        }
    }

    @NoArgsConstructor
    static class OrderValue {
        private Sort.Direction direction;
        private String property;
        private Sort.NullHandling nullHandling;

        public OrderValue(Sort.Order order) {
            this.direction = order.getDirection();
            this.property = order.getProperty();
            this.nullHandling = order.getNullHandling();
        }

        public Sort.Order toOrder() {
            return new Sort.Order(direction, property, nullHandling);
        }
    }
}
