package com.bbc.discount.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bbc.discount.entity.DiscountBean;

import jakarta.transaction.Transactional;

public interface DiscountRepository extends JpaRepository<DiscountBean, Integer> {
    // JpaRepository 提供基本 CRUD 操作
	
	// 删除所有已过期折扣的方法
    @Modifying
    @Transactional
    @Query("DELETE FROM DiscountBean d WHERE d.Dte < CURRENT_TIMESTAMP")
    int deleteExpiredDiscounts();
    
    //新增已过期折扣
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO discount (Dname, Dtype, Dpercent, Ddepart, Dts, Dte) " +
            "VALUES ('過期1', '百分比', 10, '男裝', '2025-01-01 00:00:00', '2025-01-10 12:00:00'), "
            + "('過期2', '現金', 10, '女裝', '2025-01-01 00:00:00', '2025-01-11 12:00:00'), "
            + "('過期3', '百分比', 10, '童裝', '2025-01-01 00:00:00', '2025-01-06 12:00:00'), "
            + "('過期4', '現金', 10, '無限制', '2025-01-01 00:00:00', '2025-01-01 12:00:00')",            
            nativeQuery = true)
    int insertExpiredDiscount();
    @Query("SELECT d FROM DiscountBean d WHERE " +
    		"d.Did NOT BETWEEN 1 AND 8 AND " +
            "(:Dname IS NULL OR d.Dname LIKE :Dname) AND " +
            "(:Dpercent IS NULL OR d.Dpercent = :Dpercent) AND " +
            "(:Dtype IS NULL OR d.Dtype = :Dtype) AND " +
            "(:Ddepart IS NULL OR d.Ddepart = :Ddepart) AND " +
            "(:activeTime IS NULL OR (d.Dts <= :activeTime AND d.Dte >= :activeTime))")
    List<DiscountBean> findByActiveTime(
            @Param("Dname") String Dname,
            @Param("Dpercent") Integer Dpercent,
            @Param("Dtype") String Dtype,
            @Param("Ddepart") String Ddepart,
            @Param("activeTime") LocalDateTime activeTime
    );

    @Modifying
    @Transactional
    @Query("UPDATE LuckyDrawBean l SET l.LDname = :LDname WHERE l.LDid = :LDid")
    int updateLuckyDrawName(@Param("LDid") int LDid, @Param("LDname") String LDname);
}
