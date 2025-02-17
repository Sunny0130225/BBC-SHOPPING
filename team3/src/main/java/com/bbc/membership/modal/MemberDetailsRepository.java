package com.bbc.membership.modal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface MemberDetailsRepository extends JpaRepository<MemberDetails, Integer> {
Optional<MemberDetails> findByResettoken(String resettoken);
}
