package com.example.blaze.test.dto;

import com.example.blaze.test.domain.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class OrderDto {
    private String number;
    private OrderStatus status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate date;
    private String customer;
    private Map<String, BigDecimal> taxesAmounts;
    private BigDecimal totalTaxes;
    private List<String> productIds;
}
