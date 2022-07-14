package com.example.blaze.test.repository;

import com.example.blaze.test.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface OrderRepository extends MongoRepository<Order, String> {

    @Query("{ number: { $regex: '^[0-9]' } }")
    Page<Order> findAll(Pageable pageable);

}
