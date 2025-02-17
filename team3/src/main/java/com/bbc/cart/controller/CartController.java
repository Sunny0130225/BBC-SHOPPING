package com.bbc.cart.controller;

import com.bbc.cart.entity.CartItem;
import com.bbc.cart.respository.CartItemRespository;
import com.bbc.discount.entity.DiscountBean;
import com.bbc.discount.entity.MembershipDiscountBean;
import com.bbc.discount.repository.DiscountRepository;
import com.bbc.discount.repository.MembershipDiscountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemRespository cartItemRespository;

    @Autowired
    private MembershipDiscountRepository membershipDiscountRepository;

    @Autowired
    private DiscountRepository discountRepository;

    /**
     * 查詢會員的折扣券
     */
    @GetMapping("/discounts/{memberId}")
    public ResponseEntity<List<DiscountBean>> getMemberDiscounts(@PathVariable int memberId) {
        List<MembershipDiscountBean> membershipDiscounts = membershipDiscountRepository.findAll();
        List<DiscountBean> discounts = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now(); // 取得當前時間

        for (MembershipDiscountBean md : membershipDiscounts) {
            if (md.getId() == memberId) {
                Optional<DiscountBean> discountOpt = discountRepository.findById(md.getDid());
                if (discountOpt.isPresent()) {
                    DiscountBean discount = discountOpt.get();
                    // 過濾掉未開始 (Dts) 或已過期 (Dte) 的折扣券
                    if (discount.getDts().isBefore(now) && discount.getDte().isAfter(now)) {
                        discounts.add(discount);
                    }
                }
            }
        }

        return ResponseEntity.ok(discounts);
    }

    /**
     * 查詢會員的購物車內容
     */
    @GetMapping("/items/{memberId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable int memberId) {
        List<CartItem> cartItems = cartItemRespository.findByMemberId(memberId);
        return ResponseEntity.ok(cartItems);
    }
}
