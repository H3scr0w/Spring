package com.saintgobain.dsi.pcpeg.service.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SortUtil {

    public static Pageable ignoreCase(Pageable pageable) {
        if (pageable != null && pageable.isPaged()) {
            List<Order> orders = pageable.getSort().stream()
                    .map(order -> order = order.ignoreCase())
                    .collect(Collectors.toList());
            Sort sort = Sort.by(orders);
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return pageable;
    }

    public static Pageable andSort(Pageable pageable, String sortBy) {
        if (pageable != null && pageable.isPaged()) {
            if (pageable.getSort().isUnsorted() ||
                    pageable.getSort().stream().anyMatch(order -> order.getProperty().equals(sortBy))) {
                return pageable;
            }
            Sort sort = pageable.getSort().and(Sort.by(sortBy));
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return pageable;
    }
}
