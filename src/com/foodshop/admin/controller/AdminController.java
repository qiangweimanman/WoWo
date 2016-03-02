package com.foodshop.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foodshop.admin.jms.AdminJMS;
import com.foodshop.admin.service.AdminManager;
import com.foodshop.admin.util.Sendmail;
import com.foodshop.admin.vo.Admin;

@Controller
public class AdminController {

	@Autowired(required=true)
	private AdminManager adminManager;
	
	
	@Autowired(required=true)
	private AdminJMS adminJMS;
	
	private String info;

	@RequestMapping("/login")
	public String login(String xh,String psd,HttpServletRequest request){
		
		adminJMS.sendTextMessage("this is admin,text", true);
		
		return "login";
	
	}
	@RequestMapping("/bindingMail")
	public String bindingMail(Admin admin,HttpServletRequest request) {
		
		if(adminManager.bindingMail(admin)) {
			return "redirect:/loginSuc.jsp";
		}else {
			request.setAttribute("status", "fail");
			return "bindingMail";
		}
		
	}
	
	@RequestMapping("/regist")
	public String registAdmin() {
		
		Admin admin = new Admin();
		admin.setAdmin_uname("admin");
		admin.setAdmin_pwd("admin");
		if(adminManager.regist(admin))
			return "registSuc";
		else
			return "fail";
	}
	
}
