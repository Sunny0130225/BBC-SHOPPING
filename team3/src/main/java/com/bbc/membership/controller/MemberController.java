package com.bbc.membership.controller;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bbc.membership.modal.LoginBean;
import com.bbc.membership.modal.Member;
import com.bbc.membership.modal.MemberDetails;
import com.bbc.membership.modal.MembershipService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;






@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	MembershipService mService;
	
	@PostMapping("/insert")
	@ResponseBody
	public boolean createMember(@RequestBody Member member) {
		boolean res = mService.createMember(member);
		System.out.println(res);
		return res;
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseBody
	public boolean deleteMember(@PathVariable Integer id) {
		boolean result = mService.deleteMember(id);
		if(result) {
			return true;
	}return false;
	}
	@PutMapping("/update")
	@ResponseBody
	public boolean updateMember(@RequestBody Member member) {
		boolean res = mService.updateMember(member);
		if(res) {
		return true;
		}return false;
	}
	
	@GetMapping("/search")
	@ResponseBody
	public List<Member> getMembers() {
		List<Member> members = mService.searchMembers();
		return members;
	}
	
	@GetMapping("/search/{id}")
	@ResponseBody
	public Member getMember(@PathVariable Integer id) {
		Member member = mService.searchMember(id);
		return member;
	}
	@GetMapping("/searchDetails/{id}")
	@ResponseBody
	public MemberDetails getMemberDetails(@PathVariable int id) {
		Member member = mService.searchMember(id);
		MemberDetails details = member.getDetails();
		System.out.println(details);
		return details;
	}
	
	@PostMapping("/updateDetails/{id}")
	@ResponseBody
	public boolean updateDetails(@PathVariable int id,@RequestBody MemberDetails details ){
		boolean res = mService.updateMemberDetails(id, details);
		if(res) {			
			return true;
		}return false;
		
	}
	@PostMapping("logout")
	public void logout(HttpSession session) {
		if(session!=null) {
			session.invalidate();
		}
	}
	
	@PostMapping("/login")
	public LoginBean login(@RequestBody Member member,HttpSession session) {
		boolean login = mService.login(member.getEmail(), member.getPasswords());
		if(login) {
			Member fmember = mService.findMember(member.getEmail());
			MemberDetails details = fmember.getDetails();
			System.out.println(details);
			session.setAttribute("detail", details);			
			session.setAttribute("member",fmember);
			LoginBean loginBean = new LoginBean(login, fmember.getUsername(), fmember.getId());	
			return loginBean;
		}
		LoginBean loginBean = new LoginBean(login, null, 0);
		 return loginBean;					
	}
	
	
		

}
