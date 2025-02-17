package com.bbc.cart.entity;

import org.springframework.stereotype.Component;

import com.bbc.membership.modal.Member;
import com.bbc.product.bean.ProductBean;
import com.bbc.product.bean.ProductDetailBean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="cart")
@Component
@Getter
@Setter
@ToString
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int cartItemId;
	
	private int member_id;
	
	private int productId;
	
	private int productDetail_id;
	
	private String depart;
	
	private int quantity;
	
	private double totalPrice;
	
	private String color;
	
	private String size;
	
	private String name;
	
	@Lob
	private String picture;

	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartItem(int cartItemId, int member_id, int productId, int productDetail_id, String depart, int quantity,
			double totalPrice, String color, String size, String name) {
		super();
		this.cartItemId = cartItemId;
		this.member_id = member_id;
		this.productId = productId;
		this.productDetail_id = productDetail_id;
		this.depart = depart;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.color = color;
		this.size = size;
		this.name = name;
	}
	
	
	
	
	
	
}
