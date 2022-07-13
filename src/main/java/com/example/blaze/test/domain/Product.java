package com.example.blaze.test.domain;

import com.example.blaze.test.domain.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Document(collection = "product")
public class Product {

    @Id
    private String id;

    private String name;

    private String category;

    private BigDecimal price;

    private ProductStatus status;

}
