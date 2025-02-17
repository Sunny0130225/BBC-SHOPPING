package com.bbc.membership.modal;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class LoginBean {
	private boolean success;
    private String username;
    private int memberId;

    // Constructors
    public LoginBean(boolean success, String username, int memberId) {
        this.success = success;
        this.username = username;
        this.memberId = memberId;

    }
}
