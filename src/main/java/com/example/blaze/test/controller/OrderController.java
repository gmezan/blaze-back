package com.example.blaze.test.controller;

import com.example.blaze.test.dto.AddItemsDto;
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
    public OrderDto post(@RequestBody OrderDto orderDto) {
        return this.orderService.create(orderDto);
    }

    @PutMapping(value = "/{id}")
    public OrderDto put(@PathVariable String id, @RequestBody OrderDto orderDto) {
        return this.orderService.update(id, orderDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id){
        this.orderService.delete(id);
    }

    @PostMapping(value = "/{id}/reject")
    public OrderDto reject(@PathVariable String id, @RequestBody OrderDto orderDto) {
        return this.orderService.reject(id, orderDto);
    }

    @PostMapping(value = "/{id}/complete")
    public OrderDto complete(@PathVariable String id, @RequestBody OrderDto orderDto) {
        return this.orderService.complete(id,orderDto);
    }


    @PutMapping(value = "/{number}/add/{id}")
    public OrderDto addProduct(@PathVariable String number, @PathVariable String id, @RequestBody OrderDto orderDto) {
        return this.orderService.addProduct(number, id, orderDto);
    }

    @PutMapping(value = "/{number}/delete/{id}")
    public OrderDto deleteProduct(@PathVariable String number, @PathVariable String id, @RequestBody OrderDto orderDto) {
        return this.orderService.deleteProduct(number, id,orderDto);
    }

    @PutMapping(value = "/{number}/add-items")
    public OrderDto addItems(@PathVariable String number, @RequestBody AddItemsDto itemsDto) {
        return this.orderService.addItems(number, itemsDto);
    }

}
