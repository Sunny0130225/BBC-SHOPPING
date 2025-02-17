package com.bbc.membership.modal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {

	@Autowired
	private MemberRepository mrepo;
	
	
	public boolean createMember(Member member) {
		Optional<Member> op = mrepo.findByEmail(member.getEmail());
		if(op.isPresent()) {
			return false;
		}
		mrepo.save(member);
		return true;
	}
	
	public boolean updateMember(Member member) {
		Optional<Member> op = mrepo.findById(member.getId());
		if(op.isPresent()) {
			mrepo.save(member);		
			return true;
		}return false;
	}
	
	public boolean deleteMember(int id) {
		Optional<Member> op = mrepo.findById(id);
		if(op.isPresent()) {
			mrepo.deleteById(id);
			return true;
		}
			return false;
	}
	
	public List<Member> searchMembers() {
		List <Member> members = new ArrayList<Member>();
		members = mrepo.findAll();
		return members;
	}
	
	public Member findMember(String email) {
		Optional<Member> member = mrepo.findByEmail(email);
		if (member.isPresent()) {
			return member.get();
		}return null;
	}
	public Member searchMember(int id) {
		Optional<Member> member = mrepo.findById(id);
		if (member.isPresent()) {
			return member.get();
		}return null;
	}
	
	public boolean updateMemberDetails(int id,MemberDetails details) {
		Optional<Member> op = mrepo.findById(id);
		if(op.isPresent()) {
			Member member = op.get();
			member.setDetails(details);
			mrepo.save(member);
			return true;
		}
		return false;
	}
	public boolean login(String email,String password) {
		Optional<Member> op = mrepo.findByEmail(email);
		if(op.isPresent()) {
			Member member = op.get();
			boolean equals = member.getPasswords().equals(password);
			return equals;
		}return false;
	}
	//保存圖片
	public void savePhoto(String path,int id) {
		Optional<Member> op = mrepo.findById(id);
		if(op.isPresent()) {
			Member member = op.get();
			MemberDetails details = member.getDetails();
			if(details==null) {
				MemberDetails ndetails = new MemberDetails();
				ndetails.setPicture(path);
				member.setDetails(ndetails);
				mrepo.save(member);
			}else {
				String oldPicture = details.getPicture();
				if(oldPicture!=null&&!oldPicture.isEmpty()) {
					String staticDir = System.getProperty("user.dir") + "/src/main/resources/static";
					String relativePath = oldPicture.replace("http://localhost:8081", "");
			        File oldFile = new File(staticDir + relativePath);
			        if(oldFile.exists()) {
			        	oldFile.delete();
			        	System.out.println("成功刪除圖片");
			        }					
				}
			details.setPicture(path);
			mrepo.save(member);
			}
		}
	}
}
