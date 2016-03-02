package com.foodshop.admin.service;

import com.foodshop.admin.vo.Admin;

public interface AdminManager {
	public abstract boolean bindingMail(Admin admin);
	
	public abstract Admin login(String uname,String pwd);
	
	
	public abstract boolean regist(Admin admin);
}
