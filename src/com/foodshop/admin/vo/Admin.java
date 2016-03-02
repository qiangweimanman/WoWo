package com.foodshop.admin.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="admin")
public class Admin implements Serializable{
	
	@Id  
	@Column(name="[id]")
	@SequenceGenerator(name="seqhilo",sequenceName="admin_id")
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Integer admin_id;
	
	private String admin_uname;
	
	private String admin_pwd;
	
	private long admin_tel;
	
	private String admin_mail;

	public Integer getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
	}

	public long getAdmin_tel() {
		return admin_tel;
	}

	public void setAdmin_tel(long admin_tel) {
		this.admin_tel = admin_tel;
	}

	public String getAdmin_uname() {
		return admin_uname;
	}

	public void setAdmin_uname(String admin_uname) {
		this.admin_uname = admin_uname;
	}

	public String getAdmin_pwd() {
		return admin_pwd;
	}

	public void setAdmin_pwd(String admin_pwd) {
		this.admin_pwd = admin_pwd;
	}


	public String getAdmin_mail() {
		return admin_mail;
	}

	public void setAdmin_mail(String admin_mail) {
		this.admin_mail = admin_mail;
	}
}
