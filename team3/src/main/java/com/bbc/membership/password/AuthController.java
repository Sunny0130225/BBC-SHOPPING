package com.bbc.membership.password;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbc.membership.modal.Member;
import com.bbc.membership.modal.MemberRepository;
import com.bbc.membership.modal.MembershipService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
@Autowired
private VerifyService vService ;
@Autowired
private PasswordResetTokenRepository prepo;
@Autowired
private MemberRepository mrepo;
@PostMapping("/forgot-password")
public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    boolean result = vService.sendResetPasswordEmail(email);
    
    if (result) {
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Collections.singletonMap("message", "該 Email 不存在"));
    }
}

@PostMapping("/forgot-passwordback")
public ResponseEntity<?> forgotPasswordback(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    boolean result = vService.sendResetPasswordEmailback(email);
    
    if (result) {
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Collections.singletonMap("message", "該 Email 不存在"));
    }
}
@PostMapping("/reset-password")
public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
	 try {String token = request.get("token");
    String newPassword = request.get("newPassword");

    PasswordResetToken resetToken = prepo.findByToken(token);
    if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Collections.singletonMap("message", "重設連結已過期或無效"));
    }

    // 更新密碼
    Member member = resetToken.getMember();
    member.setPasswords(newPassword);
    mrepo.save(member);

    // 刪除 token
    prepo.delete(resetToken);

    return ResponseEntity.ok(Collections.singletonMap("success", true));
	 } catch (Exception e) {
	        e.printStackTrace(); // 印出錯誤
	        return ResponseEntity.status(500).body(Map.of("message", "內部錯誤"));
	    }
}



}
