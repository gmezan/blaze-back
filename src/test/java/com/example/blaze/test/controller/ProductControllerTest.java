package com.example.blaze.test.controller;

import com.example.blaze.test.domain.Product;
import com.example.blaze.test.repository.OrderRepository;
import com.example.blaze.test.repository.ProductRepository;
import com.example.blaze.test.service.ProductService;
import com.example.blaze.test.service.ProductServiceImpl;
import com.example.blaze.test.service.SequenceGeneratorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ProductControllerTest {

    private static MockMvc mockMvc;

    private static ProductRepository productRepository;

    private static ProductController productController;

    @BeforeAll
    static void setUp() throws IOException {
        productRepository = Mockito.mock(ProductRepository.class);
        MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);


        SequenceGeneratorService sequenceGeneratorService = new SequenceGeneratorService(mongoOperations);
        ProductService productService = new ProductServiceImpl(productRepository, orderRepository, sequenceGeneratorService);
        productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void shouldReturnProductsList() throws Exception {

        final var prod = new Product();
        prod.setId("1");
        prod.setName("prod0");

        final Page<Product> page = new PageImpl<>(List.of(prod));

        Mockito.when(productRepository.findPagedProducts(Mockito.any())).thenReturn(page);

        mockMvc.perform(get("/product")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnProduct() throws Exception {

        final var prod = new Product();
        prod.setId("1");
        prod.setName("prod0");

        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(prod));

        mockMvc.perform(get("/product/1")).andDo(print()).andExpect(status().isOk());
    }

}
