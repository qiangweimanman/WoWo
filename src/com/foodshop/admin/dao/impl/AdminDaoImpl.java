package com.foodshop.admin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.foodshop.admin.dao.AdminDao;
import com.foodshop.admin.util.Sendmail;
import com.foodshop.admin.vo.Admin;


@Repository("adminDao")
public class AdminDaoImpl implements AdminDao{
	
	@PersistenceContext(name="un")
	private EntityManager em;

	@Override
	public Admin login(String uname, String pwd) {

		String jpql="select s from Student s where s.xh=:xh and s.psd=:psd";
		List<Admin> ls=em.createQuery(jpql)
				.setParameter("admin_uname", uname)
				.setParameter("admin_pwd", pwd)
				.getResultList();
		
		if(ls.isEmpty()) return null;
		else return ls.get(0);
	}

	@Override
	public boolean sendMail(Admin admin) {
		boolean flag = true;
		try {
			Sendmail send = new Sendmail(admin);
			//用户注册成功之后就使用用户注册时的邮箱给用户发送一封Email
			//发送邮件是一件非常耗时的事情，因此这里开辟了另一个线程来专门发送邮件
			send.start();
			//等待发送
			Thread.sleep(2000);
		} catch (Exception e) {
			flag = false;
		}
		
		return flag;
	}

	@Override
	public int regist(Admin admin) {
		em.persist(admin);
		return admin.getAdmin_id();
	}

}
