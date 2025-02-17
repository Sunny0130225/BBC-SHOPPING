package com.bbc.review.controller;

import com.bbc.review.model.ProductReview;
import com.bbc.review.model.ProductReviewRepository;
import com.bbc.review.model.ProductReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/review")
public class ProductReviewController {

    @Autowired
    private ProductReviewService insertService;

    @Autowired
    private ProductReviewService deleteService;

    
    @Autowired
    private ProductReviewRepository reviewRepository;

    @Autowired
    private ProductReviewService allReviewsService;

    @Autowired
    private ProductReviewService updateService;
    
    
    //	新增	http://localhost:8081/review/insert
    //	刪除	http://localhost:8081/review/delete
    //	查詢單筆	http://localhost:8081/review/int/{id}
    //	查詢所有評論	http://localhost:8081/review   
    //	更新功能的查詢評論 http://localhost:8081/review/integer/{id}
    //	更新評論		http://localhost:8081/review/{id}
    
    
    
    // 新增評論
    @PostMapping("/insert")
    public ResponseEntity<String> insertReview(@RequestPart("review") String reviewJson,
                                               @RequestPart(value = "productPicture", required = false) MultipartFile productPicture) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductReview review = objectMapper.readValue(reviewJson, ProductReview.class);

            if (productPicture != null && !productPicture.isEmpty()) {
                review.setProductPicture(productPicture.getBytes());
            }

            boolean isAdded = insertService.insertReview(review);
            return ResponseEntity.ok("{\"success\": " + isAdded + "}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"系統錯誤！\"}");
        }
    }

 

    // 刪除評論
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteReview(@RequestParam int userId,
                                                            @RequestParam int productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isDeleted = deleteService.deleteReview(userId, productId);

            if (isDeleted) {
                response.put("success", true);
                response.put("message", "評價已成功刪除！");
                response.put("userId", userId);
                response.put("productId", productId);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "刪除失敗，找不到該評價！");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "伺服器內部錯誤，請稍後再試！");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 查詢單筆
    @GetMapping("/int/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable int id) {
        Optional<ProductReview> reviewOpt = reviewRepository.findById(id);

        if (reviewOpt.isPresent()) {
            ProductReview review = reviewOpt.get();
            String base64Image = review.getProductPicture() != null
                    ? Base64.getEncoder().encodeToString(review.getProductPicture())
                    : null;

            return ResponseEntity.ok(Map.of(
                    "id", review.getId(),
                    "userId", review.getUserId(),
                    "product", review.getProduct(),
                    "productId", review.getProductId(),
                    "productPicture", base64Image,
                    "content", review.getContent(),
                    "rating", review.getRating(),
                    "status", review.getStatus(),
                    "createdAt", review.getFormattedCreatedAt(),
                    "updatedAt", review.getFormattedUpdatedAt()
            ));
        } else {
            return ResponseEntity.status(404).body("Review not found");
        }
    }

    // 查詢所有評論
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllReviews() {
        List<ProductReview> reviews = allReviewsService.getAllReviews();

        List<Map<String, Object>> response = reviews.stream().map(review -> {
            Map<String, Object> reviewMap = new HashMap<>();
            reviewMap.put("id", review.getId());
            reviewMap.put("userId", review.getUserId());
            reviewMap.put("product", review.getProduct());
            reviewMap.put("productId", review.getProductId());
            reviewMap.put("content", review.getContent());
            reviewMap.put("rating", review.getRating());
            reviewMap.put("status", review.getStatus());
            reviewMap.put("createdAt", review.getFormattedCreatedAt());
            reviewMap.put("updatedAt", review.getFormattedUpdatedAt());
            reviewMap.put("productPicture", review.getProductPictureBase64());
            return reviewMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 更新功能的查詢評論
    @GetMapping("/integer/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Integer id) {
        Optional<ProductReview> review = updateService.getReviewById(id);

        if (review.isPresent()) {
            return ResponseEntity.ok(review.get());
        } else {
            return ResponseEntity.status(404).body("{\"error\":\"找不到該評論！\"}");
        }
    }

    
    
    // 更新評論
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable Integer id,
            @Validated @RequestBody ProductReview review,
            @RequestParam(required = false) String base64Image) {

        review.setId(id); // 設置 ID
        boolean isUpdated = updateService.updateReview(review, base64Image);

        if (isUpdated) {
            ProductReview updatedReview = updateService.getReviewById(id).orElse(null);
            if (updatedReview != null) {
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "評論更新成功！",
                    "createdAt", updatedReview.getFormattedCreatedAt(),
                    "updatedAt", updatedReview.getFormattedUpdatedAt()
                ));
            }
        }
        return ResponseEntity.status(404).body("{\"status\":\"error\", \"message\":\"評論更新失敗，該 ID 不存在！\"}");
    }
    
    
    @GetMapping("/exportreviews")
    public ResponseEntity<byte[]> exportReviewsToCsv() throws IOException {
        List<ProductReview> reviews = allReviewsService.getAllReviews();  // 獲取所有評論資料
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
        // 寫入 UTF-8 BOM（這樣可以讓 Excel 正確識別編碼）
        byteArrayOutputStream.write(0xef);
        byteArrayOutputStream.write(0xbb);
        byteArrayOutputStream.write(0xbf);

        // 使用 UTF-8 編碼寫入文件
        OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);

        // 寫入 CSV 標題
        writer.write("id,user_id,product,product_id,content,rating,status,created_at,updated_at\n");

        // 使用 SimpleDateFormat 處理日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 寫入評論資料
        for (ProductReview review : reviews) {
            // 處理 content 字段中的特殊字符（如逗號和雙引號），並將雙引號轉義為兩個引號
            String content = review.getContent().replace("\"", "\"\"");  // 轉義雙引號
            
            // 僅對需要的欄位進行雙引號包裹，避免多餘的引號
            content = "\"" + content + "\"";  // 在 content 欄位加上雙引號

            // 正確的格式化，並處理所有的字段
            writer.write(String.format("%d,%d,%s,%d,%s,%d,%s,%s,%s\n",
                    review.getId(),
                    review.getUserId(),
                    review.getProduct(),
                    review.getProductId(),
                    content,  // 使用處理過的 content
                    review.getRating(),
                    review.getStatus(),
                    dateFormat.format(review.getCreatedAt()),  // 格式化日期
                    dateFormat.format(review.getUpdatedAt())));  // 格式化日期
        }

        writer.flush();
        byte[] csvData = byteArrayOutputStream.toByteArray();

        // 設定回應標頭
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reviews.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

}


