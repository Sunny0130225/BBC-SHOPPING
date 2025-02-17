package com.bbc.discount.repository;

import com.bbc.discount.entity.MembershipDiscountBean;
import com.bbc.discount.entity.MembershipDiscountId;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipDiscountRepository 
        extends JpaRepository<MembershipDiscountBean, MembershipDiscountId> {

    Optional<MembershipDiscountBean> findByIdAndDid(int id, int did); // 自定義查詢方法
}