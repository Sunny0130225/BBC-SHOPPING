package com.bbc.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.bbc.product.bean.ProductDetailBean;
import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetailBean, Integer> {
	List<ProductDetailBean> findByProductDetailIdAndDetailId(int id, int detailId);
	List<ProductDetailBean> findByProductDetail_Id(int productId);
	
}
