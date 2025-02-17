package com.bbc.product.service;

import org.springframework.stereotype.Service;

import com.bbc.product.bean.ProductBean;
import com.bbc.product.repository.ProductRepository;


import java.util.List;


@Service
public class ProductService {
	private final ProductRepository productRepository;
	

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	//顯示所有商品
	public List<ProductBean> getAllProducts() {
	    return productRepository.findAll();
	}
	//透過id查詢
	public ProductBean getProductById(int id){
		return productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
	}
	//透過名稱查詢
	public List<ProductBean> getProductByName(String name){
		return productRepository.findByName(name);
	}
	//透過部門查詢
    public List<ProductBean> getProductsByDepart(String depart) {
        return productRepository.findByDepart(depart);
    }
    //透過分類查詢
    public List<ProductBean> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    //透過季節查詢
    public List<ProductBean> getProductsBySeason(String season){
    	return productRepository.findBySeason(season);
    }
    //透過風格查詢
    public List<ProductBean> getProductsByStyle(String style){
    	return productRepository.findByStyle(style);
    }
	//新增
    public ProductBean addProduct(ProductBean product) {
        if (product.getPicture() == null || product.getPicture().isEmpty()) {
            throw new RuntimeException("商品圖片不得為空！");
        }
        
        return productRepository.save(product);
    }
 
	//刪除
	public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("未找到此id: " + id);
        }
        productRepository.deleteById(id);
    }
	//全部修改
    public ProductBean updateProduct(int id, ProductBean updatedProduct) {
        ProductBean existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此id: " + id));
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDepart(updatedProduct.getDepart());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setSeason(updatedProduct.getSeason());
        existingProduct.setStyle(updatedProduct.getStyle());
        existingProduct.setIntroduction(updatedProduct.getIntroduction());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setPicture(updatedProduct.getPicture());
        return productRepository.save(existingProduct);
    }
    //修改名稱
    public ProductBean updateProductName(int id,String name) {
    	ProductBean existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
    	existingProduct.setName(name);
    	return productRepository.save(existingProduct);
    }
    //修改部門
    public ProductBean updateProductDepart(int id,String depart) {
    	ProductBean existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
    	existingProduct.setDepart(depart);
    	return productRepository.save(existingProduct);
    }
    //修改類型
    public ProductBean updateProductCategory(int id,String category) {
    	ProductBean existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
    	existingProduct.setCategory(category);
    	return productRepository.save(existingProduct);
    }
    //修改季節
    public ProductBean updateProductSeason(int id,String season) {
    	ProductBean existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
    	existingProduct.setSeason(season);
    	return productRepository.save(existingProduct);
    }
    //修改風格
    public ProductBean updateProductStyle(int id,String style) {
    	ProductBean existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
    	existingProduct.setStyle(style);
    	return productRepository.save(existingProduct);
    }
    //修改內容
    public ProductBean updateProductIntroduction(int id,String introduction) {
    	ProductBean existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
    	existingProduct.setIntroduction(introduction);
    	return productRepository.save(existingProduct);
    }
    //修改價格
  	public ProductBean updateProductPrice(int id,float price) {
  		ProductBean existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
    	existingProduct.setPrice(price);
    	return productRepository.save(existingProduct);
  	}
  	//修改商品圖片
    public ProductBean updateProductPicture(int id, String picture) {
        ProductBean existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("未找到此 id: " + id));
        
        existingProduct.setPicture(picture);  // 更新 Base64 圖片
        return productRepository.save(existingProduct);
    }
 }
