package com.bbc.orders.service;

import java.util.List;

import com.bbc.orders.entity.OrderItem;
import com.bbc.orders.entity.Orders;

public interface OrderItemService {

	OrderItem selectOrderItem(int id);
    List<OrderItem> selectAllOrderItems();
    OrderItem insertOrderItem(OrderItem item);
    OrderItem updateOrderItem(java.util.Map<String, String> params);
    boolean deleteOrderItem(int id);
	List<OrderItem> getOrderItemsByMemberId(Integer memberId);




}
