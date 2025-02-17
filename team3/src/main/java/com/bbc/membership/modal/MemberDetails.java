package com.bbc.membership.modal;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "memberdetails")
@Component
@Getter @Setter
@ToString
public class MemberDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "realname")
	private String realname;

	@Column(name = "gender")
	private String gender;

	@Column(name = "birthday")
	private String birthday;

	@Column(name = "phone")
	private String phone;

	@Column(name = "address")
	private String address;

	@Column(name = "picture")
	private String picture;

	@Column(name = "createddate")
	private String createddate;

	@Column(name = "updateddate")
	private String updateddate;

	@Column(name = "lastlogin")
	private String lastlogin;

	@Column(name = "status")
	private String status;

	@Column(name = "role")
	private String role;

	@Column(name = "verified")
	private String verified;

	@Column(name = "reset_token")
	private String resettoken;

	@OneToOne(mappedBy = "details")
	@JsonBackReference
	@ToString.Exclude
	private Member memberBean;
	
	


}
