package com.bbc.discount.repository;

import com.bbc.discount.entity.MembershipLuckydrawBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipLuckydrawRepository extends JpaRepository<MembershipLuckydrawBean, Integer> {
}
