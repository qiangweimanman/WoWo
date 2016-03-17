package com.foodshop.admin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

public class DataUtil {
	
	public final static int STORE_STATUS_UNCHECK = -1;
	public final static int STORE_STATUS_CHECK = 0;
	public final static int STORE_STATUS_CHECKED_UNREGIST = 1;
	public final static int STORE_STATUS_CHECKED_REGIST = 2;
	public final static int STORE_STATUS_CHECK_REGISTED = 3;
	
	/**
	 * 管理员是否是第一次登录
	 */
	public static boolean ADMIN_IS_FIRST_LOGIN = false;
	/**
	 * 普通admin消息队列
	 */
	public final static int ADMIN_QUEUE = 1;
	/**
	 * 普通store消息队列
	 */
	public final static int STORE_QUEUE = 2;
	/**
	 * 普通amdin主题队列
	 */
	public final static int ADMIN_TOPIC = 3;
	/**
	 * store得到新申请书数量消息队列
	 */
	public final static int STORE_QUEUE_GET_NEWNUMBER = 4;
	// 成功申请
	public static int STORE_STATUS_SUCCESS = 1;
	// 未申请
	public static int STORE_STATUS_FAIL = 0;

	/**
	 * 新申请商家的邮箱
	 */
	public static String NEW_NUMBER_EMAIL = null;
	
	public static String JohnMd(String source, String mdType) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(mdType);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] md = digest.digest(source.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte b : md) {
			String stemp = Integer.toHexString(b);
			int len = stemp.length();
			if (len > 2) {
				stemp = stemp.substring(len - 2);

			} else if (len < 2) {
				stemp += "0";
			}
			sb.append(stemp);

		}

		return sb.toString();
	}

	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是GB2312
				String s = encode;
				return s; // 是的话，返回“GB2312“，以下代码同理
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是ISO-8859-1
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是UTF-8
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是GBK
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return ""; // 如果都不是，说明输入的内容不属于常见的编码格式。
	}
	
	/**
	 * 普通的admin消息队列
	 */
	private final static Destination queueDestination = new ActiveMQQueue("adminQueue");
	
	/**
	 * 从store消息队列中获得新申请个数信息
	 */
	private final static Destination queueDestinationGetnewNum = new ActiveMQQueue("storeQueueGetnewNum");
	/**
	 * 普通的admin主题队列
	 */
	private final static Destination topicDestination = new ActiveMQTopic("adminTopic");
	private static MessageConsumer consumer;
	private static Session session;
	public static String ReceiveMsg(int msgName) {
		String path = "tcp://115.28.88.171:61616";//蒋垚
		ConnectionFactory factory = new ActiveMQConnectionFactory(path);
		
			Connection conn = null;
			
			try {
				conn = factory.createConnection();
				conn.start();
				session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
				if (msgName == DataUtil.ADMIN_QUEUE) {
					consumer = session.createConsumer(queueDestination);
				}else if(msgName == DataUtil.ADMIN_TOPIC){
					consumer = session.createConsumer(topicDestination);
				}else if (msgName == DataUtil.STORE_QUEUE_GET_NEWNUMBER) {
					consumer = session.createConsumer(queueDestinationGetnewNum);
				}
				TextMessage message =(TextMessage)consumer.receive();
				return message.getText();
			
			}catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				
				try {
					consumer.close();
					session.close();
					conn.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		return null;
	}
}
