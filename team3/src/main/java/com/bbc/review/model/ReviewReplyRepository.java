package com.bbc.review.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Integer> {
    
	 
    
    // 獲取特定使用者的所有回覆
    List<ReviewReply> findByUserId(int userId);
    
    // 根據 ProductReview 的 id 刪除所有回覆
    void deleteByProductReview_Id(int reviewId);
    
 // 根據 ProductReview 的 id 查詢所有回覆
    List<ReviewReply> findByProductReview_Id(int reviewId);
    
}