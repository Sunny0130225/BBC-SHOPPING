package com.bbc.discount.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbc.discount.service.MembershipDiscountService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/membership-discount")
public class MembershipDiscountController {

    private final MembershipDiscountService membershipDiscountService;

    public MembershipDiscountController(MembershipDiscountService membershipDiscountService) {
        this.membershipDiscountService = membershipDiscountService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<Map<String, Object>>> getMemberCoupons(@RequestParam int memberId) {
        try {
            List<Map<String, Object>> coupons = membershipDiscountService.getMemberCoupons(memberId);
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}