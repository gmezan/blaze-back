package com.example.blaze.test.repository;

import com.example.blaze.test.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    @Query("{ number: { $regex: '^[0-9]' } }")
    Page<Order> findAll(Pageable pageable);

    @Query("{ products: { $elemMatch: { id: ?0 }}}")
    List<Order> findOrderWithProduct(String productId);

}
