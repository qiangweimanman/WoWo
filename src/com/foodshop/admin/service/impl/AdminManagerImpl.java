package com.foodshop.admin.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodshop.admin.dao.AdminDao;
import com.foodshop.admin.jms.AdminJMS;
import com.foodshop.admin.service.AdminManager;
import com.foodshop.admin.util.DataUtil;
import com.foodshop.admin.util.Sendmail;
import com.foodshop.admin.vo.Admin;
import com.foodshop.admin.vo.Store;

import net.sf.json.JSONArray;


@Service("adminManager")
public class AdminManagerImpl implements AdminManager{

	@Autowired(required=true)
	private AdminDao adminDao;
	
	@Override
	@Transactional
	public boolean bindingMail(Admin admin) {
		
		if(adminDao.login(admin.getAdmin_uname(), admin.getAdmin_pwd())!=null) {
			if (sendMail(admin)) {
				
				if(adminDao.updateMailAndTel(admin)) {
					
					return true;
				}
				
			}
		}
	
		return false;
	}
	
	@Override
	public boolean sendMail(Admin admin) {
		boolean flag = false;
		try {
			Sendmail send = new Sendmail(admin.getAdmin_mail(), "注册成功", "尊敬的" + admin.getAdmin_uname() + "您已经成功注册了，谢谢您的支持");
			//用户注册成功之后就使用用户注册时的邮箱给用户发送一封Email
			//发送邮件是一件非常耗时的事情，因此这里开辟了另一个线程来专门发送邮件
			send.start();
			//等待发送
			Thread.sleep(2000);
			flag = true;
		} catch (Exception e) {
			flag = false;
			return flag;
		}
		if (flag) {
			
		}
		
		return flag;
	}

	
	@Override
	public boolean login(String xh, String psd) {
		if (adminDao.login(xh, psd) == null) {
			return false;
		}
		return true;
		
	}

	@Override
	@Transactional
	public boolean regist(Admin admin) {
		if (adminDao.regist(admin) > 0) {
			return true;
		} 
		return false;
	}

	@Override
	@Transactional
	public boolean findStoreInfo(HttpServletResponse response,AdminJMS adminJMS) {
		
		List<Store> storeList = adminDao.findStoreInfo();
		if (storeList == null) {
			return false;
		}
		List<Store> storeFail = new ArrayList<>();
		List<Store> storeSuc = new ArrayList<>();
		for (Store store : storeList) {
			System.out.println(DataUtil.getEncoding(store.getShopName()));
			
			if (store.getStatus() > DataUtil.STORE_STATUS_CHECK) {
				storeSuc.add(store);
			}else {
				storeFail.add(store);
			}
		}
		List<Store> storeSort = storeSuc;
		
		Collections.sort(storeSort, new Comparator<Store>() {

			@Override
			public int compare(Store store1, Store store2) {
				
				int cn = - store1.getComplainNum() + store2.getComplainNum();
				if (cn != 0) {
					return cn;
				}else {
					return store1.getStoreId() - store2.getStoreId();
				}
			}
		});
		JSONArray storeSortInfo = JSONArray.fromObject(storeSort);
		JSONArray storeSucInfo = JSONArray.fromObject(storeSuc);
		JSONArray storeFailInfo = JSONArray.fromObject(storeFail);
		List<JSONArray> store = new ArrayList<>();
		store.add(storeSortInfo);
		store.add(storeSucInfo);
		store.add(storeFailInfo);
		JSONArray storeJSON = JSONArray.fromObject(store);
		try {
			PrintWriter out = response.getWriter();
			out.write(storeJSON.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}

	 
	
}
