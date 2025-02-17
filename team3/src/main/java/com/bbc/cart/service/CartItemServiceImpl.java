package com.bbc.cart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbc.cart.entity.CartItem;
import com.bbc.cart.respository.CartItemRespository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {
	
	@Autowired
	private CartItemRespository cartItemRespository;

	@Override
	public List<CartItem> getAllCartItem() {
		return cartItemRespository.findAll();
	}


	@Override
	public boolean addCartItem(CartItem cartItem) {
		CartItem savecCartItem = cartItemRespository.save(cartItem);
		return savecCartItem != null;
	}

	@Override
	public boolean updateCartItemQuantityNPrice(int cartItemid, int quantity) {
		
		if(quantity <= 0) {
			return false; 
		}
		
		Optional<CartItem> cartItem = cartItemRespository.findById(cartItemid);
		if(cartItem.isPresent()) {
			CartItem getItem = cartItem.get();
			getItem.setQuantity(quantity);
//			getItem.setTotalPrice(getItem.getTotalPrice() * quantity);
			cartItemRespository.save(getItem);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean deleteCartItem(int cartItemId) {
		Optional<CartItem> cartItem = cartItemRespository.findById(cartItemId);
		if(cartItem.isPresent()) {
			cartItemRespository.deleteById(cartItemId);
			return true;
		}else {
			return false;
		}
	}

}
