package com.bbc.membership.password;

import java.time.LocalDateTime;

import com.bbc.membership.modal.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PasswordResetToken {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); // 設置 1 小時有效期

    public PasswordResetToken(String token, Member member) {
        this.token = token;
        this.member = member;
    }

	public PasswordResetToken() {
		super();
	}
    
}
