package com.bbc.orders.dto;

import com.bbc.orders.entity.Orders;

import lombok.Data;

@Data
public class CheckoutOrderRequestDTO {

	private Integer memberId;
    private Orders order;

}
