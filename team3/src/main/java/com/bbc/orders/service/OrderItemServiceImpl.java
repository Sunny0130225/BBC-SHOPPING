package com.bbc.orders.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbc.membership.modal.Member;
import com.bbc.orders.entity.OrderItem;
import com.bbc.orders.entity.Orders;
import com.bbc.orders.repository.OrderItemRepository;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
    private OrderItemRepository orderItemRepository;

	@Override
	public OrderItem selectOrderItem(int id) {
        return orderItemRepository.findById(id).orElse(null);

	}

	@Override
	public List<OrderItem> selectAllOrderItems() {
        return orderItemRepository.findAll();

	}

	@Override
	public OrderItem insertOrderItem(OrderItem item) {
        return orderItemRepository.save(item);
	}

	@Override
	public OrderItem updateOrderItem(Map<String, String> params) {
	    // 從 params 中提取出 OrderItem ID
	    int id = Integer.parseInt(params.get("id"));

	    Optional<OrderItem> orderItem = orderItemRepository.findById(id);

	    if (orderItem.isPresent()) {
	        OrderItem updatedItem = orderItem.get();

	        // 更新訂單項目屬性
	        if (params.containsKey("quantity")) {
	            updatedItem.setQuantity(Integer.parseInt(params.get("quantity")));
	        }
	        if (params.containsKey("unitPrice")) {
	            updatedItem.setUnitPrice(Float.parseFloat(params.get("unitPrice")));
	        }
	        if (params.containsKey("discountAmount")) {
	            updatedItem.setDiscountAmount(Float.parseFloat(params.get("discountAmount")));
	        }
	        if (params.containsKey("size")) {
	            updatedItem.setSize(params.get("size"));
	        }
	        if (params.containsKey("color")) {
	            updatedItem.setColor(params.get("color"));
	        }
	     

	        // 保存並返回更新後的 OrderItem
	        return orderItemRepository.save(updatedItem);
	    }

	    return null; // 如果找不到 OrderItem，返回 null
	}

	
	@Override
	public boolean deleteOrderItem(int id) {

		Optional<OrderItem> opt = orderItemRepository.findById(id);
        if (opt.isPresent()) {
            orderItemRepository.delete(opt.get());
            return true;
        }
        return false;
	}

	@Override
	public List<OrderItem> getOrderItemsByMemberId(Integer memberId) {
		// 創建一個 Member 物件，根據 ID 查找會員
        Member member = new Member();
        member.setId(memberId);
        
        return orderItemRepository.findByMember(member);
       }



	

	
	
	
	
	}
	
	

