package com.bbc.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bbc.product.bean.ProductBean;

public interface ProductRepository extends JpaRepository<ProductBean, Integer> {
    // 自訂查詢方法

    List<ProductBean> findByName(String name);
    List<ProductBean> findByDepart(String depart);
    List<ProductBean> findByCategory(String category);
    List<ProductBean> findBySeason(String season);
    List<ProductBean> findByStyle(String style);
    
    
}
