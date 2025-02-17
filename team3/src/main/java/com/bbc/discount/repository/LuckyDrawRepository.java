package com.bbc.discount.repository;

import com.bbc.discount.entity.LuckyDrawBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuckyDrawRepository extends JpaRepository<LuckyDrawBean, Long> {
}