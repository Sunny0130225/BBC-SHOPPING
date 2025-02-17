package com.bbc.orders.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentDTO {

	 private Integer orderId;        
	 private BigDecimal finalAmount; 
	 private String productName;    
	 private String paymentMethod;   
	 private Integer memberId;       
	 
}
