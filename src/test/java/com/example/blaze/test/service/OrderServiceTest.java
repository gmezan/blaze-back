package com.example.blaze.test.service;

import com.example.blaze.test.controller.ProductController;
import com.example.blaze.test.domain.Order;
import com.example.blaze.test.dto.OrderDto;
import com.example.blaze.test.repository.OrderRepository;
import com.example.blaze.test.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class OrderServiceTest {
    private static ProductRepository productRepository;
    private static OrderRepository orderRepository;
    private static OrderService orderService;

    @BeforeAll
    static void setUp() throws IOException {
        productRepository = Mockito.mock(ProductRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);

        SequenceGeneratorService sequenceGeneratorService = new SequenceGeneratorService(mongoOperations);
        orderService = new OrderServiceImpl(orderRepository, productRepository, sequenceGeneratorService);
    }


    @Test
    public void shouldGetOrder() {
        var order = new Order();
        order.setNumber("1");
        order.setCustomer("Gustavo");
        order.setProducts(new ArrayList<>());

        OrderDto response = null;

        Mockito.when(orderRepository.findById(Mockito.any())).thenReturn(Optional.of(order));

        try {
            response = orderService.get("1");
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage(), ex);
        }

        assert response != null;
        Assertions.assertEquals(response.getCustomer(), "Gustavo");

    }
}
