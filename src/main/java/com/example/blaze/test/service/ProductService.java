package com.example.blaze.test.service;

import com.example.blaze.test.dto.ProductDto;
import org.springframework.data.domain.Page;


public interface ProductService {

    Page<ProductDto> getAll(Integer page, Integer size);

    ProductDto get(String id);

    ProductDto create(ProductDto productDto);

    ProductDto update(String id, ProductDto productDto);

    void delete(String id);

}
