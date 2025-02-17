package com.bbc.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbc.orders.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {


	List<Orders> findByMemberId(int memberId);

	List<Orders> findByMemberIdAndStatus(int memberId, String status);

	

	

	
}
