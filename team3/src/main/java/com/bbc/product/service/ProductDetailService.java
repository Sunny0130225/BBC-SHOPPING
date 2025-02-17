package com.bbc.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bbc.product.bean.ProductBean;
import com.bbc.product.bean.ProductDetailBean;
import com.bbc.product.repository.*;

@Service
public class ProductDetailService {
	private final ProductRepository productRepository;
	private final ProductDetailRepository productDetailRepository;

	public ProductDetailService(ProductRepository productRepository,ProductDetailRepository productDetailRepository) {
		this.productRepository = productRepository;
		this.productDetailRepository = productDetailRepository;
		
	}
	//顯示特定商品的所有詳細資料
	public List<ProductDetailBean> getProductDetailsByProductId(int productId) {
        return productDetailRepository.findByProductDetail_Id(productId);
    }
	public List<ProductDetailBean> getProductDetailsByProductIdAndDetailId(int id,int detailId) {
        return productDetailRepository.findByProductDetailIdAndDetailId(id,detailId);
    }
	//查詢
	public List<ProductDetailBean> getDetailsByProductId(int id) {
		if(productRepository.existsById(id)) {
			return productDetailRepository.findByProductDetail_Id(id);
		}else {
			throw new RuntimeException("未找到此id: " + id);
		}
	    
	}
	//新增
	public ProductDetailBean addProductDetail(int id,ProductDetailBean productDetail) {
		ProductBean product = productRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("未找到此 id: " + id));

	        // 設置關聯
	        productDetail.setProductDetail(product);

	    return productDetailRepository.save(productDetail);
	}
	//刪除
	public void deleteProductDetail(int id,int detailId) {
		if(productRepository.existsById(id)) {
			productDetailRepository.deleteById(detailId);
		}else {
			throw new RuntimeException("未找到此id: " + id);
		}
	}
	//更新商品詳細資料
	public ProductDetailBean updateProductDetail(int id, int detailId,ProductDetailBean updatedDetail) {
		if(productRepository.existsById(id)) {
			ProductDetailBean existingProductDetail = productDetailRepository.findById(detailId).orElseThrow(()-> new RuntimeException("未找到此detailId" + detailId));
			existingProductDetail.setColor(updatedDetail.getColor());
			existingProductDetail.setSize(updatedDetail.getSize());
			existingProductDetail.setSum(updatedDetail.getSum());
			return productDetailRepository.save(existingProductDetail);
		}else {
			throw new RuntimeException("未找到此id: " + id);
		}
	}
	//更新顏色
	public ProductDetailBean updateColor(int id,int detailId ,String color) {
		if(productRepository.existsById(id)) {
			ProductDetailBean existingProductDetail = productDetailRepository.findById(detailId).orElseThrow(()-> new RuntimeException("未找到此detailId" + detailId));
			existingProductDetail.setColor(color);
			return productDetailRepository.save(existingProductDetail); 
		}else {
			throw new RuntimeException("未找到此id: " + id);
		}
	}
	//更新尺寸
	public ProductDetailBean updateSize(int id,int detailId ,String size) {
		if(productRepository.existsById(id)) {
			ProductDetailBean existingProductDetail = productDetailRepository.findById(detailId).orElseThrow(()-> new RuntimeException("未找到此detailId" + detailId));
			existingProductDetail.setSize(size);
			return productDetailRepository.save(existingProductDetail); 
		}else {
			throw new RuntimeException("未找到此id: " + id);
		}
	}
	//更新數量
	public ProductDetailBean updateSum(int id,int detailId ,int sum) {
		if(productRepository.existsById(id)) {
			ProductDetailBean existingProductDetail = productDetailRepository.findById(detailId).orElseThrow(()-> new RuntimeException("未找到此detailId" + detailId));
			existingProductDetail.setSum(sum);
			return productDetailRepository.save(existingProductDetail); 
		}else {
			throw new RuntimeException("未找到此id: " + id);
		}
	}
}
