package com.foodshop.admin.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foodshop.admin.jms.AdminJMS;
import com.foodshop.admin.service.AdminManager;
import com.foodshop.admin.util.DataUtil;
import com.foodshop.admin.vo.Admin;
import com.foodshop.admin.vo.Store;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class AdminController {

	@Autowired(required = true)
	private AdminManager adminManager;

	@Autowired(required = true)
	private AdminJMS adminJMS;

	private String info;

	@RequestMapping("/login")
	public String login(String xh, String psd, HttpServletRequest request) {

		System.out.println("this is controller");
		System.out.println(xh + " -- " + psd);
		/**
		 * 第一次登录自启动监听程序
		 */
		if (DataUtil.ADMIN_IS_FIRST_LOGIN == false) {
			MyListenerQueue();
		}
		DataUtil.ADMIN_IS_FIRST_LOGIN = true;
		/* adminJMS.sendTextMessage("this is admin,text", true); */
		return adminManager.login(xh, psd) ? "index" : "fail";

	}

	@RequestMapping("/bindingMail")
	@ResponseBody
	public Admin bindingMail(Admin admin, HttpServletRequest request) {

		if (adminManager.bindingMail(admin)) {
			System.out.println("success");
			return admin;
		} else {
			System.out.println("fail");
			admin.setAdmin_mail(null);
			return admin;
		}

	}

	@RequestMapping("/regist")
	public String registAdmin() {

		Admin admin = new Admin();
		admin.setAdmin_uname("admin");
		admin.setAdmin_pwd("admin");
		return adminManager.regist(admin) ? "registSuc" : "fail";

	}

	@RequestMapping(value = "/fileUpload")
	public String fileUpload(HttpServletRequest request, File file_upload) {
		System.out.println("-----------");
		String path = request.getServletContext().getRealPath("/").replace("\\", "/");
		File file = new File(path + file_upload.getName());

		return "success";
	}

	/*
	 * @RequestMapping(value="/showStoreInfo") public String
	 * showStoreInfo(HttpServletResponse response) {
	 * 
	 * response.setContentType("application/json;charset=UTF-8");//防止数据传递乱码
	 * return adminManager.findStoreInfo(response,adminJMS)? "index":"login";
	 * 
	 * }
	 */
	@RequestMapping(value = "/newNumber")
	public String newNumber(HttpServletResponse response) {

		PrintWriter out;
		try {
			if (DataUtil.NEW_NUMBER_EMAIL != null) {
				out = response.getWriter();
				out.write("1");
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "index";
	}

	@RequestMapping(value = "/showStoreInfo")
	public String showStoreInfo(HttpServletResponse response) {

		return adminManager.findStoreInfo(response, adminJMS) ? "test" : "fail";
		/*
		 * adminJMS.sendTextMessage("0", false); String rep = "index"; String
		 * url = ""; do { url = adminJMS.receiveTextMessage(false);
		 * 
		 * } while (url.equals("showStoreInfo"));
		 * 
		 * String url = "http://192.168.3.133:8080/Store/showStoreInfo";
		 * System.out.println(url);
		 * 
		 * 
		 * HttpClient client = new DefaultHttpClient();
		 * 
		 * HttpUriRequest request = new HttpGet(url);
		 * 
		 * HttpResponse resp; String context; try { resp =
		 * client.execute(request); context =
		 * EntityUtils.toString(resp.getEntity(), "utf-8").trim();
		 * System.out.println(context);
		 * 
		 * PrintWriter out = response.getWriter(); out.write(context);
		 * out.flush(); out.close(); } catch (ClientProtocolException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } return rep;
		 */
	}

	private void MyListenerQueue(){

		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				/**
				 * 监听新申请
				 */
				DataUtil.NEW_NUMBER_EMAIL = DataUtil.ReceiveMsg(DataUtil.STORE_QUEUE_GET_NEWNUMBER);
				if (DataUtil.NEW_NUMBER_EMAIL != null) {

					HttpClient client = new DefaultHttpClient();

					HttpUriRequest request = new HttpGet(
							"http://127.0.0.1:8080/Store/findStoreInfoByEmail?email=" + DataUtil.NEW_NUMBER_EMAIL);

					HttpResponse resp;
					String context;
					try {
						resp = client.execute(request);
						context = EntityUtils.toString(resp.getEntity(), "utf-8").trim();
						
						
						  JSONArray json = JSONArray.fromObject(context);
						  List<Store> stores = (List<Store>)JSONArray.toCollection(json, Store.class);
						  Store store = stores.get(0);
						  System.out.println(store.toString());
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					DataUtil.NEW_NUMBER_EMAIL = null;
				}

			}
		}, 1000 * 2);
	}

}
