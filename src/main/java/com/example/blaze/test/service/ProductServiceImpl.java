package com.example.blaze.test.service;

import com.example.blaze.test.domain.Product;
import com.example.blaze.test.dto.ProductDto;
import com.example.blaze.test.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public ProductServiceImpl(ProductRepository productRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.productRepository = productRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public Page<ProductDto> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable).map(this::toProductDto);
    }

    @Override
    public ProductDto get(String id) {
        return productRepository.findById(id).map(this::toProductDto).orElse(null);
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        var product = this.toProduct(productDto);
        product.setId(String.valueOf(sequenceGeneratorService.generateSequenceProduct(Product.SEQUENCE_NAME)));
        return this.toProductDto(productRepository.save(product));
    }

    @Override
    public ProductDto update(String id, ProductDto productDto) {
        return this.productRepository.findById(id)
                .map(product -> {
                    var newP = this.toProduct(productDto);
                    newP.setId(product.getId());
                    return this.productRepository.save(newP);
                })
                .map(this::toProductDto)
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        this.productRepository.deleteById(id);
    }

    private Product toProduct(ProductDto dto) {
        var p = new Product();
        BeanUtils.copyProperties(dto, p);
        return p;
    }

    private ProductDto toProductDto(Product p) {
        var dto = new ProductDto();
        BeanUtils.copyProperties(p, dto);
        return dto;
    }
}
