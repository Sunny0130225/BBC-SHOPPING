package com.bbc.orders.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbc.membership.modal.MembershipService;
import com.bbc.orders.dto.CheckoutOrderRequestDTO;
import com.bbc.orders.dto.PaymentDTO;
import com.bbc.orders.entity.Orders;
import com.bbc.orders.service.OrderServiceImpl;
import com.bbc.orders.service.PaymentService;

@CrossOrigin(origins = "*")
@RequestMapping("/orders")
@RestController
public class OrderController {

	@Autowired
	private OrderServiceImpl orderService;
	
	@Autowired
	private MembershipService membershipService;
	
	@Autowired
	private PaymentService paymentService;
	
	// 查詢全部
	@GetMapping("/selectAll")
	public List<Orders> selectAll() {
        return orderService.selectAllOrders();
    }

	// 查詢會員的所有訂單
    @GetMapping("/member/{memberId}")
    public List<Orders> getMemberOrders(@PathVariable int memberId) {
        return orderService.getOrdersByMemberId(memberId);
    }
    
    // 查詢會員的訂單狀態
    @GetMapping("/member/{memberId}/status/{status}")
    public List<Orders> getMemberOrdersByStatus(@PathVariable int memberId, @PathVariable String status) {
        return orderService.getOrdersByMemberIdAndStatus(memberId, status);
    }
    
    //純結帳功能
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkoutOrderItem(@RequestBody CheckoutOrderRequestDTO checkoutOrderRequestDTO) {
        // 取得 Order 物件
        Orders orders = checkoutOrderRequestDTO.getOrder();
        
        // 設定會員
        orders.setMember(membershipService.searchMember(checkoutOrderRequestDTO.getMemberId()));

        // 儲存訂單
        Orders savedOrder = orderService.checkoutOrder(orders);

        // 直接回傳 orderId，讓前端更容易取得
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", savedOrder.getOrderId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	// 新增訂單 
	@PostMapping("/insert")
    public ResponseEntity<Orders> insert(@RequestBody Orders order) {
	    Orders createdOrder = orderService.insertOrder(order);
	    if (createdOrder != null) {
	        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
	    } else {
	        // 如果插入失敗，返回 400 錯誤
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
    }

	// 更新訂單
	@PostMapping("/update")
	public ResponseEntity<Orders> update(@RequestBody Map<String, String> params) {
	    Orders updatedOrder = orderService.updateOrder(params);
	    if (updatedOrder != null) {
	        return ResponseEntity.ok(updatedOrder);
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	}

	// 刪除訂單
	@DeleteMapping("/delete/{orderId}")
	public ResponseEntity<String> delete(@PathVariable int orderId) {
		
	    
		boolean isDeleted = orderService.deleteOrder(orderId);
		if (isDeleted) {
			return ResponseEntity.ok("此筆刪除成功");

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("刪除失敗");
		}
	}
	
	//接下來都沒用到
	 // ✅ 創建訂單並產生綠界付款表單
    @PostMapping("/CartToOrderWithPayment")
    public ResponseEntity<String> createOrderWithPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            // 取得綠界付款表單
            String paymentForm = paymentService.createPayment(paymentDTO);
            return ResponseEntity.ok(paymentForm);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>("訂單創建或支付處理失敗", HttpStatus.BAD_REQUEST);
        }
    }
 // ✅ 處理支付結果回調
    @PostMapping("/paymentResult")
    public String paymentReturn(@RequestParam Map<String, String> responseParams) {
        try {
            // 調用 PaymentService 處理支付回調
            paymentService.processPaymentReturn(responseParams);
            return "交易成功";
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "交易失敗: " + e.getMessage();
        }
    }
	

}
