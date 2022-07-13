package com.example.blaze.test.dto;

import com.example.blaze.test.domain.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private String id;
    private String name;
    private String category;
    private BigDecimal price;
    private ProductStatus status;
}
