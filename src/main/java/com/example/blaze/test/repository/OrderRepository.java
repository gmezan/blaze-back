package com.example.blaze.test.repository;

import com.example.blaze.test.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
