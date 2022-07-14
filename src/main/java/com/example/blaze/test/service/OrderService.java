package com.example.blaze.test.service;

import com.example.blaze.test.dto.AddItemsDto;
import com.example.blaze.test.dto.OrderDto;

import org.springframework.data.domain.Page;

public interface OrderService {

    Page<OrderDto> getAll(Integer page, Integer size);

    OrderDto get(String id);

    OrderDto create(OrderDto OrderDto);

    OrderDto update(String id, OrderDto OrderDto);

    void delete(String id);


    OrderDto reject(String id, OrderDto orderDto);

    OrderDto complete(String id, OrderDto orderDto);

    OrderDto addProduct(String number, String id, OrderDto orderDto);

    OrderDto deleteProduct(String number, String id, OrderDto orderDto);

    OrderDto addItems(String number, AddItemsDto itemsDto);
}
