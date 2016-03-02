package com.foodshop.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodshop.admin.dao.AdminDao;
import com.foodshop.admin.service.AdminManager;
import com.foodshop.admin.vo.Admin;


@Service("adminManager")
public class AdminManagerImpl implements AdminManager{

	@Autowired(required=true)
	private AdminDao adminDao;
	
	@Override
	public boolean bindingMail(Admin admin) {
		
		if(adminDao.login(admin.getAdmin_uname(), admin.getAdmin_pwd())!=null) {
			if (adminDao.sendMail(admin)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Admin login(String uname, String pwd) {
		
		
		return adminDao.login(uname,pwd);
	}

	@Override
	@Transactional
	public boolean regist(Admin admin) {
		if (adminDao.regist(admin) > 0) {
			return true;
		} 
		return false;
	}


	
}
