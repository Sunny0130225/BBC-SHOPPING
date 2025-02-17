package com.bbc.discount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bbc.discount.entity.DiscountBean;
import com.bbc.discount.repository.DiscountRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;

    @GetMapping
    public String getAllDiscounts(Model model) {
        List<DiscountBean> discounts = discountRepository.findAll()
                .stream()
                .filter(d -> d.getDid() < 1 || d.getDid() > 8) // 過濾 Did 1~8
                .toList();
        model.addAttribute("discounts", discounts);
        return "/views/discount"; // 對應 discount.jsp
    }

    @PostMapping("/save")
    public String saveOrUpdateDiscount(@ModelAttribute DiscountBean discount) {
        discountRepository.save(discount);
        return "redirect:/discounts";
    }

    @PostMapping("/delete/{id}")
    public String deleteDiscount(@PathVariable int id) {
        discountRepository.deleteById(id);
        return "redirect:/discounts";
    }
    
    @PostMapping("/deleteExpired")
    public String deleteExpiredDiscounts() {
        int deletedCount = discountRepository.deleteExpiredDiscounts();
        System.out.println("Deleted " + deletedCount + " expired discounts.");
        return "redirect:/discounts";
    }
    
    @PostMapping("/insertExpired")
    public String addExpiredDiscount() {
        int insertedCount = discountRepository.insertExpiredDiscount();
        System.out.println("Inserted " + insertedCount + " expired discount.");
        return "redirect:/discounts";
    }
    
    @GetMapping("/search")
    public String searchDiscounts(
            @RequestParam(required = false) String Dname,
            @RequestParam(required = false) Integer Dpercent,
            @RequestParam(required = false) String Dtype,
            @RequestParam(required = false) String Ddepart,
            @RequestParam(required = false) String activeTime, // 活動時間
            Model model) {

        // 處理 activeTime 為空的情況
        LocalDateTime activeDateTime = (activeTime != null && !activeTime.isEmpty())
                ? LocalDateTime.parse(activeTime)
                : null;

        // 使用 Repository 的查詢方法
        List<DiscountBean> discounts = discountRepository.findByActiveTime(
                Dname != null && !Dname.isEmpty() ? "%" + Dname + "%" : null,
                Dpercent,
                Dtype != null && !Dtype.isEmpty() ? Dtype : null,
                Ddepart != null && !Ddepart.isEmpty() ? Ddepart : null,
                activeDateTime
        );

        // 傳遞結果到 JSP
        model.addAttribute("discounts", discounts);
        return "/views/discount"; // 返回到主頁面
    }
}

