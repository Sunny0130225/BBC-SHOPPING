package com.bbc.product.controller;

import org.springframework.web.bind.annotation.*;

import com.bbc.product.bean.*;
import com.bbc.product.service.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    public ProductDetailController(ProductDetailService productDetailService) {
        this.productDetailService = productDetailService;
    }

    // 查詢某商品的所有詳細資料
    @GetMapping("/{id}/details")
    public List<ProductDetailBean> getProductDetailsByProductId(@PathVariable int id) {
        return productDetailService.getProductDetailsByProductId(id);
    }
    //查詢特定商品的特定詳細資料
    @GetMapping("/{id}/details/{detailId}")
    public List<ProductDetailBean> getProductDetailsByProductIdAndDetailId(@PathVariable int id,@PathVariable int detailId) {
        return productDetailService.getProductDetailsByProductIdAndDetailId(id,detailId);
    }

    // 新增商品詳細資料
    @PostMapping("/{id}/details")
    public ProductDetailBean addProductDetail(@PathVariable int id, @RequestBody ProductDetailBean detail) {
        return productDetailService.addProductDetail(id, detail);
    }

    // 刪除商品詳細資料
    @DeleteMapping("/{id}/details/{detailId}")
    public void deleteProductDetail(@PathVariable int id, @PathVariable int detailId) {
        productDetailService.deleteProductDetail(id, detailId);
    }

    // 更新商品詳細資料
    @PutMapping("/{id}/details/{detailId}")
    public ProductDetailBean updateProductDetail(@PathVariable int id, @PathVariable int detailId, @RequestBody ProductDetailBean updatedDetail) {
        return productDetailService.updateProductDetail(id, detailId, updatedDetail);
    }

    // 更新顏色
    @PatchMapping("/{id}/details/{detailId}/color")
    public ProductDetailBean updateColor(@PathVariable int id, @PathVariable int detailId, @RequestBody String color) {
        return productDetailService.updateColor(id, detailId, color);
    }

    // 更新尺寸
    @PatchMapping("/{id}/details/{detailId}/size")
    public ProductDetailBean updateSize(@PathVariable int id, @PathVariable int detailId, @RequestBody String size) {
        return productDetailService.updateSize(id, detailId, size);
    }

    // 更新數量
    @PatchMapping("/{id}/details/{detailId}/sum")
    public ProductDetailBean updateSum(@PathVariable int id, @PathVariable int detailId, @RequestBody int sum) {
        return productDetailService.updateSum(id, detailId, sum);
    }
}
