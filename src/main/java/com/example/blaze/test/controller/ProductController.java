package com.example.blaze.test.controller;

import com.example.blaze.test.dto.ProductDto;
import com.example.blaze.test.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductDto> get(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return this.productService.getAll(page, size);
    }

    @GetMapping(value = "/{id}")
    public ProductDto get(@PathVariable String id) {
        return this.productService.get(id);
    }

    @PostMapping()
    public ProductDto post(@RequestBody ProductDto productDto) {
        return this.productService.create(productDto);
    }

    @PutMapping(value = "/{id}")
    public ProductDto put(@PathVariable String id, @RequestBody ProductDto productDto) {
        return this.productService.update(id, productDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        try {
            this.productService.delete(id);
            return ResponseEntity.ok(id);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
