package com.bbc.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbc.cart.entity.CartItem;
import com.bbc.cart.service.CartItemService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cartItem")
public class CartItemController {

	@Autowired
	private CartItemService cartItemService;
	
	//根據購物車ID得到購物車商品
	@GetMapping("/selectAll")
	public List<CartItem> selectAll() {
        return cartItemService.getAllCartItem();
    }
	
	//新增商品到購物車
	@PostMapping("/add")
	public ResponseEntity<String> addCartItem(@RequestBody CartItem cartItem){
		boolean isAdd = cartItemService.addCartItem(cartItem);
		if(isAdd) {
			return ResponseEntity.status(HttpStatus.CREATED).body("商品成功加入購物車");
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("加入購物車失敗");
		}
	}
	
	//更新購物車商品數量
		@PutMapping("/update/{cartItemId}")
		public ResponseEntity<String> updateCartItemQuantity(@PathVariable int cartItemId,@RequestParam int quantity){
			boolean isUpdate = cartItemService.updateCartItemQuantityNPrice(cartItemId, quantity);
			
			if(isUpdate) {
				return ResponseEntity.ok("商品數量新增成功");
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("商品未找到");
			}
		}
	
	//刪除購物車商品
	@DeleteMapping("/delete/{cartItemId}")
	public ResponseEntity<String> deleteCartItem(@PathVariable int cartItemId){
		boolean isDelete = cartItemService.deleteCartItem(cartItemId);
		
		if(isDelete) {
			return ResponseEntity.ok("商品刪除成功");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("商品未找到");
		}
		
	}
	
	
	
}
