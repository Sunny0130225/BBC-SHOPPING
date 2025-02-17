package com.bbc.orders.dto;

import com.bbc.orders.entity.OrderItem;

import lombok.Data;

@Data
public class OrderItemRequestDTO2 {
    private Integer memberId;
    private Integer orderId;
    private Integer productId;
//    private OrderItem orderItem;
    private Integer quantity;
    private String size;
    private String color;
    private float unitPrice;
    private float subTotal;
    private float discountAmount;
}
