package com.example.blaze.test.domain;

import com.example.blaze.test.domain.enums.ProductCategory;
import com.example.blaze.test.domain.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Document(collection = "products")
public class Product {

    @Transient
    public static final String SEQUENCE_NAME = "products_sequence";

    @Id
    private String id;

    private int seq;

    private String name;

    private ProductCategory category;

    private BigDecimal price;

    private ProductStatus status;

}
