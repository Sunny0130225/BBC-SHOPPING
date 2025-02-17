package com.bbc.cart.service;

import java.util.List;

import com.bbc.cart.entity.CartItem;

public interface CartItemService {
	List<CartItem> getAllCartItem();
	boolean addCartItem(CartItem cartItem);
	boolean updateCartItemQuantityNPrice(int cartItemid, int quantity);
	boolean deleteCartItem(int cartItemId);
}
