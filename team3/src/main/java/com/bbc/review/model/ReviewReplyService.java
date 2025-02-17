package com.bbc.review.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbc.review.controller.ReviewReplyRequest;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewReplyService {

    @Autowired
    private ReviewReplyRepository reviewReplyRepository;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Transactional
    public boolean insertReply(ReviewReply reply, int reviewId) {
        // 確認對應的評論是否存在
        Optional<ProductReview> optionalReview = productReviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            // 設置對應的 ProductReview
            reply.setProductReview(optionalReview.get());
            reply.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            reviewReplyRepository.save(reply);
            return true;
        } else {
            // 如果沒有找到對應的 ProductReview，返回 false 並可以記錄錯誤
            System.out.println("錯誤：指定的 reviewId 沒有對應的 ProductReview");
            return false; // 回傳失敗
        }
    }


    // 刪除特定回覆
    @Transactional
    public boolean deleteReply(int replyId) {
        if (reviewReplyRepository.existsById(replyId)) {
            reviewReplyRepository.deleteById(replyId);
            return true;
        }
        return false;
    }

    // 查詢某貼文的所有評論
    @Transactional(readOnly = true)
    public List<ReviewReply> getRepliesByReviewId(int reviewId) {
        return reviewReplyRepository.findByProductReview_Id(reviewId);
    }
    
    
    // 獲取特定回覆
    @Transactional(readOnly = true)
    public Optional<ReviewReply> getReplyById(int replyId) {
        return reviewReplyRepository.findById(replyId);
    }

    // 獲取所有回覆
    @Transactional(readOnly = true)
    public List<ReviewReply> getAllReplies() {
        return reviewReplyRepository.findAll();
    }

    // 更新回覆
    @Transactional
    public boolean updateReply(int replyId, ReviewReplyRequest request) {
        Optional<ReviewReply> existingReplyOpt = reviewReplyRepository.findById(replyId);
        if (existingReplyOpt.isPresent()) {
            ReviewReply existingReply = existingReplyOpt.get();
            
            // 查找對應的 ProductReview
            ProductReview productReview = productReviewRepository.findById(request.getReviewId())
                    .orElseThrow(() -> new IllegalArgumentException("ProductReview 不存在！"));

            // 更新回覆的欄位
            existingReply.setContent(request.getContent());
            existingReply.setUserId(request.getUserId());
            existingReply.setProductReview(productReview);
            existingReply.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            
            // 保存更新
            reviewReplyRepository.save(existingReply);
            return true;
        }
        return false;
    }

}
