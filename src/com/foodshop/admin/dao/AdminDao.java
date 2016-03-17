package com.foodshop.admin.dao;

import java.util.List;

import com.foodshop.admin.vo.Admin;
import com.foodshop.admin.vo.Store;

public interface AdminDao {
	public abstract Admin login(String uname,String pwd);

	public abstract int regist(Admin admin);

	public abstract boolean updateMailAndTel(Admin admin);

	public abstract List<Store> findStoreInfo();

	public abstract void updateStoreStaute(String email);
}
