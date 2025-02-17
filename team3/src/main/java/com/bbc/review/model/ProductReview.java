package com.bbc.review.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "product_review") // 對應的資料表名稱
@Component
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
    @Column(name = "id")
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "product", nullable = false, length = 255)
    private String product;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Lob // 標示為大型物件
    @Column(name = "product_picture")
    private byte[] productPicture; // 圖片數據欄位

    
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "rating", nullable = false)
    private byte rating;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // 添加 @PreUpdate 方法來自動設置 updated_at
    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public String getFormattedCreatedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return createdAt != null ? sdf.format(createdAt) : "";
    }

    public String getFormattedUpdatedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return updatedAt != null ? sdf.format(updatedAt) : "";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProductReview [id=");
        builder.append(id);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", product=");
        builder.append(product);
        builder.append(", productId=");
        builder.append(productId);
        builder.append(", content=");
        builder.append(content);
        builder.append(", rating=");
        builder.append(rating);
        builder.append(", status=");
        builder.append(status);
        builder.append(", createdAt=");
        builder.append(createdAt);
        builder.append(", updatedAt=");
        builder.append(updatedAt);
        builder.append(", productPicture=");
        builder.append(productPicture != null ? "Present" : "Null");
        builder.append("]");
        return builder.toString();
    }

    public ProductReview() {
    }

    public ProductReview(int id, int userId, String product, int productId, byte[] productPicture, String content, byte rating, String status,
                         Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.productId = productId;
        this.productPicture = productPicture;
        this.content = content;
        this.rating = rating;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public byte[] getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(byte[] productPicture) {
        this.productPicture = productPicture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    
    
    @OneToMany(mappedBy = "productReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewReply> replies = new ArrayList<>();

    public List<ReviewReply> getReplies() {
        return replies;
    }

    public ProductReview(int id, int userId, String product, int productId, byte[] productPicture, String content,
			byte rating, String status, Timestamp createdAt, Timestamp updatedAt, List<ReviewReply> replies) {
		super();
		this.id = id;
		this.userId = userId;
		this.product = product;
		this.productId = productId;
		this.productPicture = productPicture;
		this.content = content;
		this.rating = rating;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.replies = replies;
	}

	public void setReplies(List<ReviewReply> replies) {
        this.replies = replies;
    }

    public void addReply(ReviewReply reply) {
        this.replies.add(reply);
        reply.setProductReview(this);
    }

    public void removeReply(ReviewReply reply) {
        this.replies.remove(reply);
        reply.setProductReview(null);
    }

    
    
    
    
    
    
    
    
 // 新增方法以獲取圖片的 Base64 字串
    public String getProductPictureBase64() {
        return (productPicture != null && productPicture.length > 0)
            ? Base64.getEncoder().encodeToString(productPicture)
            : "";
    }

   

    
    
    public boolean updateProductPicture(String base64Image) {
        if (base64Image != null && !base64Image.isEmpty()) {
            this.productPicture = Base64.getDecoder().decode(base64Image);
            return true;
        }
        return false;
    }
    
}
