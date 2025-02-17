package com.bbc.cart.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bbc.cart.entity.CartItem;
@Repository
public interface CartItemRespository extends JpaRepository<CartItem, Integer> {
   
	@Query("SELECT c FROM CartItem c WHERE c.member_id = :memberId")
    List<CartItem> findByMemberId(@Param("memberId") int memberId);
}
