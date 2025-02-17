package com.bbc.review.model;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository repository;

    // 插入新評價
    public boolean insertReview(ProductReview review) {
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        review.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        repository.save(review);
        return true;
    }

    
    
    
    // 刪除指定的評價
    @Transactional
    public boolean deleteReview(int userId, int productId) {
        if (repository.existsByUserIdAndProductId(userId, productId)) {
            repository.deleteByUserIdAndProductId(userId, productId);
            return true;
        }
        return false;
    }

    
  
    
    
    // 更新評價
    @Transactional
    public boolean updateReview(ProductReview review, String base64Image) {
        if (repository.existsById(review.getId())) {
            review.setUpdatedAt(new Timestamp(System.currentTimeMillis())); // 更新時間

            // 如果有新的圖片，進行 Base64 解碼並更新
            if (base64Image != null && !base64Image.isEmpty()) {
                review.setProductPicture(Base64.getDecoder().decode(base64Image));
            }

            repository.save(review); // 保存更新
            return true;
        }
        return false;
    }

    public Optional<ProductReview> getReviewById(Integer id) {
        return repository.findById(id);
    }


    // 根據字串 ID 查詢評價（處理格式不正確的情況）
    public Optional<ProductReview> getReviewById(String id) {
        try {
            int reviewId = Integer.parseInt(id);
            return repository.findById(reviewId);
        } catch (NumberFormatException e) {
            return Optional.empty(); // 如果 ID 格式無效，返回空 Optional
        }
    }

    // 獲取所有評價
    @Transactional(readOnly = true)
    public List<ProductReview> getAllReviews() {
        return repository.findAll();
    }
}
