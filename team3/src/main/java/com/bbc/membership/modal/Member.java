package com.bbc.membership.modal;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "membership")
@Component
@Getter
@Setter
@ToString
public class Member {
	
	
	private String email;

	
	private String passwords;

	
	private String username;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "detail_id")
	@JsonManagedReference
	private MemberDetails details;

	
	
	public Member() {
		super();
	}



	public Member(String email, String passwords, String username) {
		super();
		this.email = email;
		this.passwords = passwords;
		this.username = username;
	}
	
	

}
