package com.foodshop.admin.dao;

import org.dom4j.tree.BackedList;

import com.foodshop.admin.vo.Admin;

public interface AdminDao {
	public abstract Admin login(String uname,String pwd);
	public abstract boolean sendMail(Admin admin);
	public abstract int regist(Admin admin);
}
