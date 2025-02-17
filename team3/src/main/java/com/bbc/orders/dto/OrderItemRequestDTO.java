package com.bbc.orders.dto;

import com.bbc.orders.entity.OrderItem;

import lombok.Data;

@Data
public class OrderItemRequestDTO {
    private Integer memberId;
    private Integer orderId;
    private Integer productId;
    private OrderItem orderItem;
    
}
