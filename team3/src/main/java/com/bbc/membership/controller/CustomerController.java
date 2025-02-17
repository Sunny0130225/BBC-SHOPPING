package com.bbc.membership.controller;




import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.bbc.membership.modal.LoginBean;
import com.bbc.membership.modal.Member;
import com.bbc.membership.modal.MemberDetails;
import com.bbc.membership.modal.MembershipService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;






@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/front")
public class CustomerController {
	
	@Autowired
	MembershipService mService;
	 // 文件存儲的根目錄（建議使用配置文件管理）
	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/upload";

	
	
	@PostMapping("/insert")
	public boolean createMember(@RequestBody Member member) {
		boolean res = mService.createMember(member);
		return res;
	}
	
	 @PostMapping("/upload")
	    public String uploadFile(@RequestParam("photo") MultipartFile file,@RequestParam int id) {	        
	            // 確保目錄存在
	            File uploadDir = new File(UPLOAD_DIR);
	            if (!uploadDir.exists()) {
	                uploadDir.mkdirs(); //如果沒有此路徑就創建對應資料夾
	            }
	            // 獲取原始文件名
	            String originalFilename = file.getOriginalFilename();

	            // 為文件生成唯一名稱，避免重名
	            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

	            // 文件存儲路徑
	            String filePath = UPLOAD_DIR + "/" + uniqueFilename;

	            // 保存文件到磁碟
	            try {
					file.transferTo(new File(filePath));
				} catch (IllegalStateException e) {
					System.out.println("controller有問題");
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("controller有問題");
					e.printStackTrace();
				}
	            // 將文件路徑返回給前端或存入資料庫（相對路徑）
	            String relativePath = "http://localhost:8081"+"/upload/" + uniqueFilename;
	            mService.savePhoto(relativePath, id);
	            return relativePath; // 返回絕對路徑
	        } 
	    
	@GetMapping("getImg/{id}")
	public String getImg(@PathVariable int id) {
		Member member = mService.searchMember(id);
		String picture = member.getDetails().getPicture();
		return picture;
	}
	
	@DeleteMapping("/delete/{id}")
	public boolean deleteMember(@PathVariable Integer id) {
		boolean result = mService.deleteMember(id);
		if(result) {
			return true;
	}return false;
	}
	@PutMapping("/update")
	public boolean updateMember(@RequestBody Member member) {
		boolean res = mService.updateMember(member);
		if(res) {
		return true;
		}return false;
	}
	
	@GetMapping("/search")
	public List<Member> getMembers() {
		List<Member> members = mService.searchMembers();
		return members;
	}
	
	@GetMapping("/search/{id}")
	public Member getMember(@PathVariable Integer id) {
		Member member = mService.searchMember(id);
		return member;
	}
	@GetMapping("/searchDetails/{id}")
	public MemberDetails getMemberDetails(@PathVariable int id) {
		Member member = mService.searchMember(id);
		MemberDetails details = member.getDetails();
		System.out.println(details);
		return details;
	}
	
	@PostMapping("/updateDetails/{id}")
	public boolean updateDetails(@PathVariable int id,@RequestBody MemberDetails details ){
		boolean res = mService.updateMemberDetails(id, details);
		if(res) {			
			return true;
		}return false;
		
	}
	
	@PostMapping("/login")
	public LoginBean login(@RequestBody Member member,HttpSession session) {
		boolean login = mService.login(member.getEmail(), member.getPasswords());
		if(login) {
			Member fmember = mService.findMember(member.getEmail());
			LoginBean loginBean = new LoginBean(login, fmember.getUsername(), fmember.getId());
			return loginBean;
		}
		LoginBean loginBean = new LoginBean(login, null, 0);
		 return loginBean;					
	}
	
	
		

}
