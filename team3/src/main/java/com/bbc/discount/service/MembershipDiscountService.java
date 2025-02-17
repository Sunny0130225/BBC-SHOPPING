package com.bbc.discount.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bbc.discount.repository.MembershipDiscountRepository;

@Service
public class MembershipDiscountService {

    private final JdbcTemplate jdbcTemplate;

    public MembershipDiscountService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getMemberCoupons(int memberId) {
        String sql = """
            SELECT 
                d.Did AS ID,
                d.Dname AS 名稱,
                d.Dpercent AS 折扣,
                d.Ddepart AS 限制,
                md.remaining AS 使用次數,
                d.Dte AS 到期日
            FROM 
                membership_discount md 
            JOIN 
                discount d ON md.Did = d.Did
            WHERE 
                md.id = ?
            """;

        return jdbcTemplate.queryForList(sql, memberId);
    }
}