package com.example.blaze.test.service;

import com.example.blaze.test.dto.OrderDto;

import org.springframework.data.domain.Page;

public interface OrderService {

    Page<OrderDto> getAll(Integer page, Integer size);

    OrderDto get(String id);

    OrderDto create(OrderDto OrderDto);

    OrderDto update(String id, OrderDto OrderDto);

    void delete(String id);




}
