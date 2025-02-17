package com.bbc.orders.entity;

import com.bbc.membership.modal.Member;
import com.bbc.product.bean.ProductBean;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderItem {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Orders order;
	
	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;
	
	@ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductBean productBean;
	
	@Column(name = "quantity")
    private int quantity;
	
	@Column(name = "size")
    private String size;
	
	@Column(name = "color")
    private String color;
	
	@Column(name = "unit_price")
    private float unitPrice;
	
	@Column(name = "discount_amount")
    private float discountAmount;
	
	@Column(name = "sub_total")
    private float subTotal;
	
	
	
}
