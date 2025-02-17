package com.bbc.product.bean;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
@Getter
@Setter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name="product")
public class ProductBean {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="depart")
	private String depart;
	@Column(name="category")
	private String category;
	@Column(name="season")
	private String season;
	@Column(name="style")
	private String style;
	@Column(name="introduction")
	private String introduction;
	@Column(name="price")
	private float price;
	@Lob
	@Column(name="picture")
	private String picture;
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy="productDetail",orphanRemoval = true)
	@JsonManagedReference
	private Set<ProductDetailBean> productDetails = new LinkedHashSet<ProductDetailBean>();
	
	//建構建構子
	public ProductBean() {}
	public ProductBean(int id,String name,String depart,String category,String season,String style,String introduction,float price,Set<ProductDetailBean> productDetails, String picture) {
		super();
		this.id =id;
		this.name = name;
		this.depart = depart;
		this.category = category;
		this.season = season;
		this.style = style;
		this.introduction = introduction;
		this.price = price;
		this.productDetails = productDetails;
		this.picture = picture;
	}
	}