package com.bbc.product.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "detailId")
@Entity
@Table(name="product_details")
public class ProductDetailBean {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="detailId")
	private int detailId;	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id",nullable = false)
	@JsonBackReference
	private ProductBean productDetail;
	@Column(name="size")
	private String size;
	@Column(name="color")
	private String color;

	@Column(name="sum")
	private int sum;
	
	//建構建構子
	public ProductDetailBean() {
		
	}
	public ProductDetailBean(int detailId,ProductBean productDetail,String size,String color,int sum) {
		super();
		this.detailId =detailId;
		this.productDetail =productDetail;
		this.size = size;
		this.color = color;
		this.sum = sum;
	}
	
}