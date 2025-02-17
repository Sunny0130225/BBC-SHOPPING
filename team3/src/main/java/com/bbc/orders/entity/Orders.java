package com.bbc.orders.entity;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.bbc.discount.entity.DiscountBean;
import com.bbc.membership.modal.Member;
import com.bbc.product.bean.ProductBean;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="orders")
@Component
@Getter
@Setter
@ToString
public class Orders {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private int orderId;

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<OrderItem> orderItems; 
	
	@ManyToOne
	@JoinColumn(name = "discount_id", referencedColumnName = "Did")
	private DiscountBean discountBean;
	
	@Column(name="orderDate")
    private LocalDate orderDate;
	
	@Column(name = "payment_method", length = 500)
    private String paymentMethod;
	
	@Column(name="pickupStore")
    private String pickupStore;

	@Column(name="pickupDate")
    private LocalDate pickupDate;
	
	@Column(name = "status", length = 500)
	private String status;


	public Orders(int orderId, Member member, LocalDate orderDate, String paymentMethod,
			int unpaidAmount, String pickupStore, LocalDate pickupDate, String status) {
		super();
		this.orderId = orderId;
		this.member = member;
		this.orderDate = orderDate;
		this.paymentMethod = paymentMethod;
		this.pickupStore = pickupStore;
		this.pickupDate = pickupDate;
		this.status = status;
	}
	
	public Orders() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
