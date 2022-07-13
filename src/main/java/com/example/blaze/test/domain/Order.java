package com.example.blaze.test.domain;

import com.example.blaze.test.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/*
● Order number
● Status(Pending, Completed, Rejected)
● Date
● Customer
● Taxes amounts
● Total Taxes
● Total Amount
● List of order items
 */

@Getter
@Setter
@Document(collection = "order")
public class Order {

    @Id
    private String number;
    private OrderStatus status;
    private String orderDate;
    private String customer;
    private Map<String,BigDecimal> taxesAmounts;
    private BigDecimal totalTaxes;
    private List<String> productIds;
}
