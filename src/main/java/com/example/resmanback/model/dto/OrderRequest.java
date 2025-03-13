package com.example.resmanback.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    private String customerName;
    private LocalDateTime orderDate;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long dishId;
        private int quantity;
        private Double price;
    }
}
