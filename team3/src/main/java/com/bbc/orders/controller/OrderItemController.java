package com.bbc.orders.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbc.membership.modal.Member;
import com.bbc.membership.modal.MembershipService;
import com.bbc.orders.dto.OrderItemRequestDTO;
import com.bbc.orders.dto.OrderItemRequestDTO2;
import com.bbc.orders.entity.OrderItem;
import com.bbc.orders.entity.Orders;
import com.bbc.orders.service.OrderItemService;
import com.bbc.orders.service.OrderService;
import com.bbc.product.bean.ProductBean;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/orderItems")
public class OrderItemController {

	@Autowired
    private OrderItemService orderItemService;
	
	@Autowired
    private MembershipService membershipService;
	
	@Autowired
    private OrderService orderService;

	//顯示所有訂單明細
	@GetMapping("/selectAllItems")
	public List<OrderItem> selectAll() {
        return orderItemService.selectAllOrderItems();  // 直接返回資料，會自動轉為 JSON 格式

    }
	
	 // 根據會員ID獲取訂單項目
    @GetMapping("/selectByMemberId/{memberId}")
    public List<OrderItem> getOrderItemsByMemberId(@PathVariable Integer memberId) {
        return orderItemService.getOrderItemsByMemberId(memberId);
    }
	
	
	//新增後台訂單
	@PostMapping("/insert")
	public ResponseEntity<OrderItem> insertOrderItem(@RequestBody OrderItemRequestDTO orderItemRequestDTO) {
		OrderItem orderItem = orderItemRequestDTO.getOrderItem();
		Member member = new Member();
		member.setId(orderItemRequestDTO.getMemberId());
		orderItem.setMember(member);
		
		Orders order = new Orders();
		order.setOrderId(orderItemRequestDTO.getOrderId());
		orderItem.setOrder(order);
		
		ProductBean productBean = new ProductBean();
		productBean.setId(orderItemRequestDTO.getProductId());
		orderItem.setProductBean(productBean);
		
        OrderItem insertedItem = orderItemService.insertOrderItem(orderItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(insertedItem);
    }
	
	//新增結帳訂單
		@PostMapping("/checkoutItem")
		public ResponseEntity<OrderItem> checkoutItemOrderItem(@RequestBody OrderItemRequestDTO2 orderItemRequestDTO2) {
//			OrderItem orderItem = orderItemRequestDTO.getOrderItem();
	        OrderItem orderItem = new OrderItem();

			Member member = new Member();
			member.setId(orderItemRequestDTO2.getMemberId());
			orderItem.setMember(member);
			
			Orders order = new Orders();
			order.setOrderId(orderItemRequestDTO2.getOrderId());
			orderItem.setOrder(order);
			
			ProductBean productBean = new ProductBean();
			productBean.setId(orderItemRequestDTO2.getProductId());
			orderItem.setProductBean(productBean);
			
			orderItem.setQuantity(orderItemRequestDTO2.getQuantity());
		    orderItem.setSize(orderItemRequestDTO2.getSize());
		    orderItem.setColor(orderItemRequestDTO2.getColor());
		    orderItem.setUnitPrice(orderItemRequestDTO2.getUnitPrice());
		    orderItem.setSubTotal(orderItemRequestDTO2.getSubTotal());
		    orderItem.setDiscountAmount(orderItemRequestDTO2.getDiscountAmount());
			
	        OrderItem insertedItem = orderItemService.insertOrderItem(orderItem);
	        return ResponseEntity.status(HttpStatus.CREATED).body(insertedItem);
	    }
	
	 //更新
	 @PostMapping("/update")
	 public ResponseEntity<OrderItem> updateOrderItem(@RequestBody Map<String, String> params) {
	        OrderItem updatedItem = orderItemService.updateOrderItem(params);
	        if (updatedItem != null) {
	            return ResponseEntity.ok(updatedItem);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }
	    }
	
	
	//刪除
	 @DeleteMapping("/delete/{id}")
	    public ResponseEntity<Boolean> delete(@PathVariable int id) {
	        boolean deleted = orderItemService.deleteOrderItem(id);
	        if (deleted) {
	            return ResponseEntity.ok(true);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
	        }
	    }
}
