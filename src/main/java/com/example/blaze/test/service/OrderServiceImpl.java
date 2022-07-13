package com.example.blaze.test.service;

import com.example.blaze.test.domain.Order;
import com.example.blaze.test.dto.OrderDto;
import com.example.blaze.test.repository.OrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public Page<OrderDto> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable).map(this::toOrderDto);
    }

    @Override
    public OrderDto get(String id) {
        return orderRepository.findById(id).map(this::toOrderDto).orElse(null);
    }

    @Override
    public OrderDto create(OrderDto OrderDto) {
        return this.toOrderDto(orderRepository.save(this.toOrder(OrderDto)));
    }

    @Override
    public OrderDto update(String id, OrderDto OrderDto) {
        return this.orderRepository.findById(id)
                .map(product -> {
                    var newP = this.toOrder(OrderDto);
                    newP.setNumber(product.getNumber());
                    return this.orderRepository.save(newP);
                })
                .map(this::toOrderDto)
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        this.orderRepository.deleteAll();
    }

    private Order toOrder(OrderDto dto) {
        var o = new Order();
        BeanUtils.copyProperties(dto, o);
        o.setOrderDate(dto.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        return o;
    }

    private OrderDto toOrderDto(Order o) {
        var dto = new OrderDto();
        BeanUtils.copyProperties(o, dto);
        dto.setDate(LocalDate.parse(o.getOrderDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        return dto;
    }
}
