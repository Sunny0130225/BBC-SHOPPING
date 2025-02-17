package com.bbc.membership.password;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bbc.membership.modal.Member;
import com.bbc.membership.modal.MemberRepository;

@Service
public class VerifyService {
@Autowired
private MemberRepository mrepo;
@Autowired
private JavaMailSender mailSender;
@Autowired
private PasswordResetTokenRepository prepo;

public boolean sendResetPasswordEmail(String email) {
    Optional<Member> memberop = mrepo.findByEmail(email);
    if(!memberop.isPresent()) {
    	return false;// 用戶不存在
    }Member member = memberop.get();
 // 生成重置密碼 Token
    String token = UUID.randomUUID().toString();
    PasswordResetToken resetToken = new PasswordResetToken(token, member);
    prepo.save(resetToken);
 // 發送郵件
    String resetUrl = "http://localhost:5174/customer/reset-password?token=" + token;
    String subject = "重設您的密碼";
    String message = "請點擊以下連結重設您的密碼：" + resetUrl;
    try {
        sendEmail(email, subject, message);
        return true;
    } catch (Exception e) {
        System.err.println("郵件發送失敗：" + e.getMessage());
        return false;
    }    
}    
public boolean sendResetPasswordEmailback(String email) {
    Optional<Member> memberop = mrepo.findByEmail(email);
    if(!memberop.isPresent()) {
    	return false;// 用戶不存在
    }Member member = memberop.get();
 // 生成重置密碼 Token
    String token = UUID.randomUUID().toString();
    PasswordResetToken resetToken = new PasswordResetToken(token, member);
    prepo.save(resetToken);
 // 發送郵件
    String resetUrl = "http://localhost:5173/reset-password?token=" + token;
    String subject = "重設您的密碼";
    String message = "請點擊以下連結重設您的密碼：" + resetUrl;
    try {
        sendEmail(email, subject, message);
        return true;
    } catch (Exception e) {
        System.err.println("郵件發送失敗：" + e.getMessage());
        return false;
    }    
}    
private void sendEmail(String to, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    mailSender.send(message);
}
}

