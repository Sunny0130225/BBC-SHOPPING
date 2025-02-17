package com.bbc.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bbc.product.bean.*;
import com.bbc.product.service.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductController {

	 private final ProductService productService;
	 
	 public ProductController(ProductService productService) {
		 this.productService = productService;
	 }
	// 查詢所有商品
	 @GetMapping
	 public List<ProductBean> getAllProducts() {
	     return productService.getAllProducts();
	    }
	 //查詢id產品
	 @GetMapping("/{id}")
	 public ProductBean getProductById(@PathVariable int id) {
	     return productService.getProductById(id);
	 }
	 //查詢同館別所有產品
	 @GetMapping("/depart")
	 public ResponseEntity<List<ProductBean>> getProductsByDepart(@RequestParam String depart) {
	     List<ProductBean> product = productService.getProductsByDepart(depart);
	     return ResponseEntity.ok(product);
	 }
	 //查詢同類別所有產品
	 @GetMapping("/category")
	 public ResponseEntity<List<ProductBean>> getProductsByCategory(@RequestParam String category){
		 List<ProductBean> product = productService.getProductsByCategory(category);
		 return ResponseEntity.ok(product);
	 }
	 //查詢同季節所有產品
	 @GetMapping("/season")
	 public ResponseEntity<List<ProductBean>> getProductsBySeason(@RequestParam String season){
		 List<ProductBean> product = productService.getProductsBySeason(season);
		 return ResponseEntity.ok(product);
	 }
	 //查詢同風格所有產品
	 @GetMapping("/style")
	 public ResponseEntity<List<ProductBean>> getProductsByStyle(@RequestParam String style){
		 List<ProductBean> product = productService.getProductsByStyle(style);
		 return ResponseEntity.ok(product);
	 }
	 //新增資料
	 @PostMapping
	 public ResponseEntity<ProductBean> addProduct(@RequestBody ProductBean product) {
		    if (product.getPicture() == null || product.getPicture().isEmpty()) {
		        return ResponseEntity.badRequest().body(null); // 確保圖片不為空
		    }
		    
		    ProductBean savedProduct = productService.addProduct(product);
		    return ResponseEntity.ok(savedProduct);
		}
	 //刪除資料
	 @DeleteMapping("/{id}")
	 public void deleteProduct(@PathVariable int id) {
	     productService.deleteProduct(id);
	 }
	 //更改所有資料
	 @PutMapping("/{id}")
	 public ProductBean updateProduct(@PathVariable int id, @RequestBody ProductBean updatedProduct) {
	     return productService.updateProduct(id, updatedProduct);
	 }
	 //更改名稱
	 @PatchMapping("/{id}/name")
	 public ProductBean updateProductName(@PathVariable int id, @RequestBody String name) {
	     return productService.updateProductName(id, name);
	 }
	 //更改部門
	 @PatchMapping("/{id}/depart")
	 public ProductBean updateProductDepart(@PathVariable int id, @RequestBody String depart) {
	     return productService.updateProductDepart(id, depart);
	 }
	 //更改類型
	 @PatchMapping("/{id}/category")
	 public ProductBean updateProductCategory(@PathVariable int id, @RequestBody String category) {
	     return productService.updateProductCategory(id, category);
	 }
	 //更改季節
	 @PatchMapping("/{id}/season")
	 public ProductBean updateProductSeason(@PathVariable int id, @RequestBody String season) {
		 return productService.updateProductSeason(id, season);
	 }
	 //更改風格
	 @PatchMapping("/{id}/style")
	 public ProductBean updateProductStyle(@PathVariable int id, @RequestBody String style) {	
		 return productService.updateProductStyle(id, style);
	 }
	//更改風格
		 @PatchMapping("/{id}/introduction")
		 public ProductBean updateProductIntroduction(@PathVariable int id, @RequestBody String introduction) {	
			 return productService.updateProductIntroduction(id, introduction);
		 }
	//更改類型	
	 @PatchMapping("/{id}/price")
	 public ProductBean updateProductPrice(@PathVariable int id, @RequestBody float price) {
		 return productService.updateProductPrice(id, price);
		 }
	 //更改圖片
	 @PutMapping("/{id}/picture")
	    public ResponseEntity<ProductBean> updateProductImage(
	        @PathVariable int id, @RequestBody ProductBean product) {
	        ProductBean updatedProduct = productService.updateProductPicture(id, product.getPicture());
	        return ResponseEntity.ok(updatedProduct);
	    }
	
	 
}
