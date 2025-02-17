package com.bbc.review.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
	
	// 自訂刪除方法
    void deleteByUserIdAndProductId(int userId, int productId);

    // 確認評價是否存在
    boolean existsByUserIdAndProductId(int userId, int productId);

    // 獲取特定使用者的所有評價
    List<ProductReview> findByUserId(int userId);

    // 獲取特定商品的所有評價
    List<ProductReview> findByProductId(int productId);
    
    
    
    
}
