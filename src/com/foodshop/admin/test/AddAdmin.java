package com.foodshop.admin.test;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.foodshop.admin.service.AdminManager;
import com.foodshop.admin.vo.Admin;


public class AddAdmin {
	@BeforeClass
	public static void beforeClass() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		AdminManager adminManager = (AdminManager) applicationContext.getBean("adminManager");
		Admin admin = new Admin();
		admin.setAdmin_uname("admin");
		admin.setAdmin_pwd("admin");
		adminManager.regist(admin);
	}
	@Test
	public void testFind(){
		
		
		
		
	}
}
