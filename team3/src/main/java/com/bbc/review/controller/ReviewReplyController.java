package com.bbc.review.controller;

import com.bbc.review.model.ProductReview;
import com.bbc.review.model.ReviewReply;
import com.bbc.review.model.ReviewReplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviewReply")
public class ReviewReplyController {

    @Autowired
    private ReviewReplyService insertService;

    @Autowired
    private ReviewReplyService deleteService;

    @Autowired
    private ReviewReplyService allRepliesService;

    @Autowired
    private ReviewReplyService updateService;
    
    @Autowired
    private ReviewReplyService reviewReplyService;

    
    //	新增	http://http://localhost:8081/reviewReply/insert 新增OK
    //	刪除	http://localhost:8081/reviewReply/delete?replyId=6  刪除ok
    //	查詢單筆	http://localhost:8081/reviewReply/{id}  查詢單筆ok
    //	查詢所有回覆	http://localhost:8081/reviewReply   查詢全部ok
    //  查詢某貼文的所有留言	http://localhost:8081/reviewReply/repliesByReview/{reviewId} 查詢ok
    //	更新回覆		http://localhost:8081/reviewReply/{id}   
    
    
    

    // 新增回覆
    @PostMapping("/insert")
    public ResponseEntity<String> insertReply(@RequestBody ReviewReplyRequest request) {
        try {
            // 校驗傳入參數
            if (request.getReviewId() <= 0 || request.getUserId() <= 0 || request.getContent() == null || request.getContent().isEmpty()) {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"請確認資料是否完整\"}");
            }

            // 確保找到對應的 ProductReview
            ProductReview productReview = new ProductReview();
            productReview.setId(request.getReviewId());

            // 使用單一時間戳確保一致性
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            ReviewReply reply = new ReviewReply();
            reply.setUserId(request.getUserId());
            reply.setContent(request.getContent());
            reply.setProductReview(productReview);
            reply.setCreatedAt(currentTimestamp); // 設定相同時間戳
            reply.setUpdatedAt(currentTimestamp);

            boolean isAdded = insertService.insertReply(reply, request.getReviewId());
            if (isAdded) {
                return ResponseEntity.ok("{\"success\": true}");
            } else {
                return ResponseEntity.status(400).body("{\"success\": false, \"message\": \"找不到對應的評論，插入失敗！\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"系統錯誤！\"}");
        }
    }

    // 刪除回覆
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteReply(@RequestParam int replyId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isDeleted = deleteService.deleteReply(replyId);

            if (isDeleted) {
                response.put("success", true);
                response.put("message", "回覆已成功刪除！");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "刪除失敗，找不到該回覆！");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "伺服器內部錯誤，請稍後再試！");
            return ResponseEntity.status(500).body(response);
        }
    }

 // 查詢單筆回覆
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReplyById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ReviewReply> optionalReply = allRepliesService.getReplyById(id);

            if (optionalReply.isPresent()) {
                ReviewReply reply = optionalReply.get();
                response.put("success", true);
                response.put("id", reply.getId());
                response.put("reviewId", reply.getProductReview().getId());
                response.put("userId", reply.getUserId());
                response.put("content", reply.getContent());
                response.put("createdAt", reply.getFormattedCreatedAt());
                response.put("updatedAt", reply.getFormattedUpdatedAt());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "找不到對應的回覆！"); // 沒有找到該回覆
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "伺服器內部錯誤，請稍後再試！");
            return ResponseEntity.status(500).body(response);
        }
    }

    
    // 查詢所有回覆
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllReplies() {
        List<ReviewReply> replies = allRepliesService.getAllReplies();

        List<Map<String, Object>> response = replies.stream().map(reply -> {
            Map<String, Object> replyMap = new HashMap<>();
            replyMap.put("id", reply.getId());
            replyMap.put("reviewId", reply.getProductReview().getId());
            replyMap.put("userId", reply.getUserId());
            replyMap.put("content", reply.getContent());
            replyMap.put("createdAt", reply.getFormattedCreatedAt());
            replyMap.put("updatedAt", reply.getFormattedUpdatedAt());
            return replyMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    
    
 // 查詢某貼文的所有評論
    @GetMapping("/repliesByReview/{reviewId}")
    public ResponseEntity<List<Map<String, Object>>> getRepliesByReviewId(@PathVariable int reviewId) {
        List<ReviewReply> replies = reviewReplyService.getRepliesByReviewId(reviewId);

        // 如果沒有留言，返回成功響應並附帶訊息
        if (replies.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonList(Map.of(
                "success", true,
                "message", "此貼文尚無任何留言。"
            )));
        }

        List<Map<String, Object>> response = replies.stream().map(reply -> {
            Map<String, Object> replyMap = new HashMap<>();
            replyMap.put("id", reply.getId());
            replyMap.put("reviewId", reply.getProductReview().getId());
            replyMap.put("userId", reply.getUserId());
            replyMap.put("content", reply.getContent());
            replyMap.put("createdAt", reply.getFormattedCreatedAt());
            replyMap.put("updatedAt", reply.getFormattedUpdatedAt());
            return replyMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }



 // 更新回覆
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateReply(@PathVariable int id,
                                                           @Validated @RequestBody ReviewReplyRequest request) {
        try {
            // 校驗參數是否正確
            if (request.getReviewId() <= 0 || request.getContent() == null || request.getContent().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "請確認資料是否完整"
                ));
            }

            // 查找對應的回覆
            boolean isUpdated = updateService.updateReply(id, request);

            if (isUpdated) {
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "回覆更新成功！"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "回覆更新失敗，該 ID 不存在！"
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "message", "伺服器內部錯誤，請稍後再試！"
            ));
        }
    }

    
    @GetMapping("/exportreplies")
    public ResponseEntity<byte[]> exportRepliesToCsv() throws IOException {
        List<ReviewReply> replies = allRepliesService.getAllReplies();  // 獲取所有留言資料
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
        // 使用 BOM 指示 UTF-8 編碼
        byteArrayOutputStream.write(0xef);
        byteArrayOutputStream.write(0xbb);
        byteArrayOutputStream.write(0xbf);

        // 使用 UTF-8 編碼寫入文件
        OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);

        // 寫入 CSV 標題
        writer.write("id,review_id,user_id,content,created_at,updated_at\n");

        // 使用 SimpleDateFormat 處理日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 寫入留言資料
        for (ReviewReply reply : replies) {
            // 處理內容中的特殊字符（如雙引號）並用雙引號包裹
            String content = reply.getContent().replace("\"", "\"\"");  // 處理雙引號
            content = "\"" + content + "\"";  // 用雙引號包裹內容

            writer.write(String.format("%d,%d,%d,%s,%s,%s\n",
                    reply.getId(),
                    reply.getProductReview().getId(),  // 獲取對應的評論 ID
                    reply.getUserId(),
                    content,
                    dateFormat.format(reply.getCreatedAt()),  // 格式化日期
                    dateFormat.format(reply.getUpdatedAt())));  // 格式化日期
        }

        writer.flush();
        byte[] csvData = byteArrayOutputStream.toByteArray();

        // 設定回應標頭
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=replies.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    
 
    
}
    
