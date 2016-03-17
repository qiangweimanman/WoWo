package com.foodshop.admin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.foodshop.admin.dao.AdminDao;
import com.foodshop.admin.util.Sendmail;
import com.foodshop.admin.vo.Admin;
import com.foodshop.admin.vo.Store;


@Repository("adminDao")
public class AdminDaoImpl implements AdminDao{
	
	@PersistenceContext(name="un")
	private EntityManager em;

	@Override
	public Admin login(String xh, String psd) {

		String jpql="select s from admin s where s.admin_uname=:xh and s.admin_pwd=:psd";
		List<Admin> ls=em.createQuery(jpql)
				.setParameter("xh", xh)
				.setParameter("psd", psd)
				.getResultList();
		
		if(ls.isEmpty()) return null;
		else return ls.get(0);

	}

	
	@Override
	public boolean updateMailAndTel(Admin admin) {
		/*em.refresh(admin);
		return true;*/
		int n = 0;

		String jpql = "update admin a set a.admin_uname=:name,a.admin_pwd=:passwd,"
				+ "a.admin_tel=:phone,a.admin_mail=:email";

		n = em.createQuery(jpql).setParameter("name", admin.getAdmin_uname())
				.setParameter("passwd", admin.getAdmin_pwd())
				.setParameter("phone", admin.getAdmin_tel())
				.setParameter("email", admin.getAdmin_mail()).executeUpdate();

		return true;

	}


	@Override
	public int regist(Admin admin) {
		em.persist(admin);
		return admin.getAdmin_id();
	}


	@Override
	public List<Store> findStoreInfo() {
		String jpql="select s from store s";
		List<Store> ls = em.createQuery(jpql).getResultList();
		System.out.println("this is dao : " + ls.get(0).getShopName());
		return ls;

	}


	@Override
	public void updateStoreStaute(String email) {


		String jpql = "update store a set a.admin_distribution=:distribution";

		em.createQuery(jpql).setParameter("distribution", true).executeUpdate();

		
	}

}
