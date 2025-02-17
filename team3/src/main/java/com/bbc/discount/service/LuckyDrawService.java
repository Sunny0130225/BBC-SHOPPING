package com.bbc.discount.service;

import com.bbc.discount.entity.LuckyDrawBean;
import com.bbc.discount.entity.MembershipDiscountBean;
import com.bbc.discount.entity.MembershipDiscountId;
import com.bbc.discount.entity.MembershipLuckydrawBean;
import com.bbc.discount.repository.LuckyDrawRepository;
import com.bbc.discount.repository.MembershipDiscountRepository;
import com.bbc.discount.repository.MembershipLuckydrawRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LuckyDrawService {

    @Autowired
    private LuckyDrawRepository luckyDrawRepository;
    
    @Autowired
    private MembershipDiscountRepository membershipDiscountRepository;
    
    @Autowired
    private MembershipLuckydrawRepository membershipLuckydrawRepository;

    public List<LuckyDrawBean> getAllLuckyDraws() {
        return luckyDrawRepository.findAll();
    }
    
    public boolean decreaseLDmax(Long id) {
        Optional<LuckyDrawBean> optionalLuckyDraw = luckyDrawRepository.findById(id);
        if (optionalLuckyDraw.isPresent()) {
        	LuckyDrawBean luckyDraw = optionalLuckyDraw.get();
            if (luckyDraw.getLDmax() > 0) {
                luckyDraw.setLDmax(luckyDraw.getLDmax() - 1);
                luckyDrawRepository.save(luckyDraw);
                return true;
            }
        }
        return false;
    }
    
    @Transactional
    public void saveWinningInfo(int memberId, int discountId) {
        // 檢查是否已存在該會員的該折價券記錄
        Optional<MembershipDiscountBean> existingRecord =
                membershipDiscountRepository.findByIdAndDid(memberId, discountId);

        if (existingRecord.isPresent()) {
            // 如果已存在，增加 remaining 次數
            MembershipDiscountBean discount = existingRecord.get();
            discount.setRemaining(discount.getRemaining() + 1);
            membershipDiscountRepository.save(discount);
        } else {
            // 如果不存在，新增一筆記錄
            MembershipDiscountBean newDiscount = new MembershipDiscountBean();
            newDiscount.setId(memberId);
            newDiscount.setDid(discountId);
            newDiscount.setRemaining(1);
            membershipDiscountRepository.save(newDiscount);
        }
    }
    
 // 檢查用戶的剩餘抽獎次數
    public int getRemainingCount(int memberId) {
        Optional<MembershipLuckydrawBean> membership = membershipLuckydrawRepository.findById(memberId);
        return membership.map(MembershipLuckydrawBean::getCount).orElse(0); // 如果沒有該用戶，返回0
    }

    // 減少用戶的抽獎次數
    public boolean decreaseCount(int memberId) {
        Optional<MembershipLuckydrawBean> membership = membershipLuckydrawRepository.findById(memberId);
        if (membership.isPresent()) {
            MembershipLuckydrawBean userLuckydraw = membership.get();
            if (userLuckydraw.getCount() > 0) {
                userLuckydraw.setCount(userLuckydraw.getCount() - 1);
                membershipLuckydrawRepository.save(userLuckydraw);
                return true;
            }
        }
        return false; // 沒有該用戶或剩餘次數為0
    }
}