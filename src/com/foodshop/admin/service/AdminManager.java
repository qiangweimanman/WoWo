package com.foodshop.admin.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodshop.admin.jms.AdminJMS;
import com.foodshop.admin.vo.Admin;

public interface AdminManager {
	public abstract boolean bindingMail(Admin admin);
	
	public abstract boolean login(String uname,String pwd);
	
	
	public abstract boolean regist(Admin admin);
	public boolean sendMail(Admin admin) ;

	public abstract boolean findStoreInfo(HttpServletResponse response, AdminJMS adminJMS);
}
