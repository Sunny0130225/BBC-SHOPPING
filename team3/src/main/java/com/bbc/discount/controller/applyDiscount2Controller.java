package com.bbc.discount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bbc.discount.entity.DiscountBean;
import com.bbc.discount.entity.LuckyDrawBean;
import com.bbc.discount.repository.DiscountRepository;
import com.bbc.discount.repository.LuckyDrawRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/applyDiscount2")
public class applyDiscount2Controller {

    @Autowired
    private DiscountRepository discountRepository;
    
    @Autowired
    private LuckyDrawRepository luckyDrawRepository;

    @GetMapping
    public String getSpecialDiscounts(Model model) {
        List<DiscountBean> specialDiscounts = discountRepository.findAll()
                .stream()
                .filter(d -> d.getDid() >= 1 && d.getDid() <= 8) // 只選擇 Did 1~8
                .toList();
        model.addAttribute("specialDiscounts", specialDiscounts);
        return "/views/applyDiscount2"; // 對應 special_discount.jsp
    }

    @PostMapping("/save")
    public String saveSpecialDiscount(@ModelAttribute DiscountBean discount) {
        if (discount.getDid() >= 1 && discount.getDid() <= 8) {
            discountRepository.save(discount);

            // 查找 luckydraw 內對應的 LDid
            Optional<LuckyDrawBean> luckyDrawOptional = luckyDrawRepository.findById((long) discount.getDid());
            if (luckyDrawOptional.isPresent()) {
                LuckyDrawBean luckyDraw = luckyDrawOptional.get();
                luckyDraw.setLDname(discount.getDname()); // 更新 LDname
                luckyDrawRepository.save(luckyDraw); // 儲存修改
            }
        }
        return "redirect:/applyDiscount2";
    }
}
