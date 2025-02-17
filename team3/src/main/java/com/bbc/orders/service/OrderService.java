package com.bbc.orders.service;

import java.util.List;
import java.util.Map;

import com.bbc.orders.entity.Orders;

public interface OrderService {
	Orders selectOrder(int id);
    List<Orders> selectAllOrders();
    List<Orders> getOrdersByMemberId(int memberId);
    List<Orders> getOrdersByMemberIdAndStatus(int memberId, String status);
    Orders checkoutOrder(Orders order);
    Orders insertOrder(Orders order);
    Orders updateOrder(Map<String, String> params);
    boolean deleteOrder(int id);

}
