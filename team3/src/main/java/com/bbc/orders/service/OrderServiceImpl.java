package com.bbc.orders.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbc.orders.entity.OrderItem;
import com.bbc.orders.entity.Orders;
import com.bbc.orders.repository.OrderItemRepository;
import com.bbc.orders.repository.OrderRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Override
	public Orders selectOrder(int id) {
		Optional<Orders> product = orderRepository.findById(id);

		if (product.isPresent()) {
			return product.get();
		}
		return null;
	}

	@Override
	public List<Orders> selectAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Orders checkoutOrder(Orders order) {
	    return orderRepository.save(order); 
	}
	
	@Override
	public List<Orders> getOrdersByMemberId(int memberId) {
        return orderRepository.findByMemberId(memberId);
	}

	@Override
	public List<Orders> getOrdersByMemberIdAndStatus(int memberId, String status) {
        return orderRepository.findByMemberIdAndStatus(memberId, status);
	}
	
	@Override
	public Orders insertOrder(Orders order) {
	    return orderRepository.save(order);
	}

	public Orders updateOrder(Map<String, String> params) {
	    // 從 params 中提取出訂單 ID
	    int id = Integer.parseInt(params.get("orderId"));
	    
	    Optional<Orders> order = orderRepository.findById(id);
	    
	    if (order.isPresent()) {
	        Orders updatedOrder = order.get();

	        // 更新訂單屬性
	        if (params.containsKey("orderDate")) {
	            updatedOrder.setOrderDate(LocalDate.parse(params.get("orderDate")));
	        }
	        if (params.containsKey("paymentMethod")) {
	            updatedOrder.setPaymentMethod(params.get("paymentMethod"));
	        }
	        if (params.containsKey("pickupStore")) {
	            updatedOrder.setPickupStore(params.get("pickupStore"));
	        }
	        if (params.containsKey("pickupDate")) {
	            updatedOrder.setPickupDate(LocalDate.parse(params.get("pickupDate")));
	        }
	        if (params.containsKey("status")) {
	            updatedOrder.setStatus(params.get("status"));
	        }

	        // 保存並返回更新後的訂單
	        return orderRepository.save(updatedOrder); 
	    }
	    return null; // 如果找不到訂單，返回 null
	}


	@Override
	public boolean deleteOrder(int id) {
		Optional<Orders> order = orderRepository.findById(id);
		if (order.isPresent()) {
			orderRepository.delete(order.get());
			return true;
		}
		return false;
	}

	

	

	
	

}
