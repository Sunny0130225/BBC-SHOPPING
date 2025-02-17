package com.bbc.discount.controller;

import com.bbc.discount.entity.LuckyDrawBean;
import com.bbc.discount.service.LuckyDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/luckydraw")
@CrossOrigin(origins = "*")
public class LuckyDrawController {

    @Autowired
    private LuckyDrawService luckyDrawService;

    @GetMapping
    public List<LuckyDrawBean> getLuckyDraws() {
        return luckyDrawService.getAllLuckyDraws();
    }
    @GetMapping("/remainingCount")
    public ResponseEntity<Integer> getRemainingCount(@RequestParam("memberId") int memberId) {
        int remaining = luckyDrawService.getRemainingCount(memberId);
        return ResponseEntity.ok(remaining);
    }
    @PostMapping("/decrease")
    public ResponseEntity<String> decreaseLDmax(@RequestParam Long id) {
        boolean updated = luckyDrawService.decreaseLDmax(id);
        if (updated) {
            return ResponseEntity.ok("LDmax updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update LDmax");
        }
    }
    @PostMapping("/saveWinningInfo")
    public ResponseEntity<String> saveWinningInfo(
            @RequestParam("memberId") int memberId,
            @RequestParam("discountId") int discountId) {
        try {
            boolean updated = luckyDrawService.decreaseCount(memberId);
            if (!updated) {
                return ResponseEntity.badRequest().body("No remaining count available.");
            }
            luckyDrawService.saveWinningInfo(memberId, discountId);
            return ResponseEntity.ok("Winning info saved successfully. Count decreased.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save winning info: " + e.getMessage());
        }
    }
}