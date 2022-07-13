package com.example.blaze.test.controller;

import com.example.blaze.test.dto.OrderDto;
import com.example.blaze.test.service.OrderService;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;


@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Page<OrderDto> get(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return this.orderService.getAll(page, size);
    }

    @GetMapping(value = "/{id}")
    public OrderDto get(@PathVariable String id) {
        return this.orderService.get(id);
    }

    @PostMapping()
    public OrderDto post(@RequestBody OrderDto OrderDto) {
        return this.orderService.create(OrderDto);
    }

    @PutMapping(value = "/{id}")
    public OrderDto put(@PathVariable String id, @RequestBody OrderDto OrderDto) {
        return this.orderService.update(id, OrderDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id){
        this.orderService.delete(id);
    }
    
}
