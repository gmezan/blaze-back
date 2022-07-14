package com.example.blaze.test.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddItemsDto {
    private List<OrderDto.ProductOrder> itemsSelected;
}
