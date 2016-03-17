package com.foodshop.admin.jms.impl;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;
import org.springframework.stereotype.Service;

import com.foodshop.admin.jms.AdminJMS;
import com.foodshop.admin.util.DataUtil;
import com.mysql.fabric.xmlrpc.base.Data;

@Service("adminJMS")
public class AdminJMSImpl implements AdminJMS{


	@Resource(name="pooledConnectionFactory")
	private ConnectionFactory factory;
	/**
	 * 普通的admin消息队列
	 */
	@Resource(name="queueDestination")
	private  ActiveMQDestination queueDestination;
	
	/**
	 * 从store消息队列中获得新申请个数信息
	 */
	@Resource(name="queueDestinationGetnewNum")
	private  ActiveMQDestination queueDestinationGetnewNum;
	/**
	 * 普通的admin主题队列
	 */
	@Resource(name="topicDestination")
	private ActiveMQDestination topicDestination;
	
	private Connection conn;
	private Session session;
	private Message msg;
	private MessageProducer producer;
	private MessageConsumer consumer;

	
	@Override
	public boolean sendTextMessage(String context,int msgName) {
		
		boolean flag = false;
		try {
			conn = factory.createConnection();
			conn.start();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			msg = session.createTextMessage(context);
			if (msgName == DataUtil.ADMIN_QUEUE) {
				producer = session.createProducer(queueDestination);
			}else if(msgName == DataUtil.ADMIN_TOPIC){
				producer = session.createProducer(topicDestination);
			}else if (msgName == DataUtil.STORE_QUEUE_GET_NEWNUMBER) {
				producer = session.createProducer(queueDestinationGetnewNum);
			}
			producer.send(msg);
			flag = true;
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			closeJMS(producer, consumer, session, conn);
		}
		
		return flag;
	}




	@Override
	public boolean sendMapMessage(MapMessage mapContext, int msgName) {

		try {
			conn = factory.createConnection();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			if (msgName == DataUtil.ADMIN_QUEUE) {
				producer = session.createProducer(queueDestination);
			}else if(msgName == DataUtil.ADMIN_TOPIC){
				producer = session.createProducer(topicDestination);
			}else if (msgName == DataUtil.STORE_QUEUE_GET_NEWNUMBER) {
				producer = session.createProducer(queueDestinationGetnewNum);
			}
			producer.send(mapContext);  
			return true;
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			closeJMS(producer, consumer, session, conn);
		}
		return false;
	}
	

	@Override
	public boolean sendObjectMessage(Object objectContext, int msgName) {
		try {
			conn = factory.createConnection();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			ObjectMessage objMsg=session.createObjectMessage((Serializable) objectContext);//发送对象时必须让该对象实现serializable接口  
			if (msgName == DataUtil.ADMIN_QUEUE) {
				producer = session.createProducer(queueDestination);
			}else if(msgName == DataUtil.ADMIN_TOPIC){
				producer = session.createProducer(topicDestination);
			}else if (msgName == DataUtil.STORE_QUEUE_GET_NEWNUMBER) {
				producer = session.createProducer(queueDestinationGetnewNum);
			}  
			producer.send(objMsg);
			return true;
		} catch (JMSException e) {
			e.printStackTrace();
		} 
		return false;
	}


	@Override
	public String receiveTextMessage(int msgName) {
		
		String text = null;
		try {
			conn = factory.createConnection();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			conn.start();
			if (msgName == DataUtil.ADMIN_QUEUE) {
				consumer = session.createConsumer(queueDestination);
			}else if(msgName == DataUtil.ADMIN_TOPIC){
				consumer = session.createConsumer(topicDestination);
			}else if (msgName == DataUtil.STORE_QUEUE_GET_NEWNUMBER) {
				consumer = session.createConsumer(queueDestinationGetnewNum);
			}
			msg = consumer.receive();
			if (msg instanceof TextMessage) {
				text =  ((TextMessage) msg).getText();
			}
		
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			closeJMS(producer, consumer, session, conn);
		}
		
		return text;
	}

	@Override
	public MapMessage receiveMapMessage(int msgName) {
		MapMessage mapMessage = null;
		try {
			conn = factory.createConnection();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			conn.start();
			if (msgName == DataUtil.ADMIN_QUEUE) {
				consumer = session.createConsumer(queueDestination);
			}else if(msgName == DataUtil.ADMIN_TOPIC){
				consumer = session.createConsumer(topicDestination);
			}else if (msgName == DataUtil.STORE_QUEUE_GET_NEWNUMBER) {
				consumer = session.createConsumer(queueDestinationGetnewNum);
			}
			msg = consumer.receive();
			if (msg instanceof MapMessage) {
				mapMessage = (MapMessage)msg;
			}
		
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			closeJMS(producer, consumer, session, conn);
		}
		
		return mapMessage;
	}



	@Override
	public ObjectMessage receiveObjectMessage(int msgName) {
		ObjectMessage message = null;
		try {
			conn = factory.createConnection();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			conn.start();
			if (msgName == DataUtil.ADMIN_QUEUE) {
				consumer = session.createConsumer(queueDestination);
			}else if(msgName == DataUtil.ADMIN_TOPIC){
				consumer = session.createConsumer(topicDestination);
			}else if (msgName == DataUtil.STORE_QUEUE_GET_NEWNUMBER) {
				consumer = session.createConsumer(queueDestinationGetnewNum);
			}
			msg = consumer.receive();
			if (msg instanceof ObjectMessage) {
				message = (ObjectMessage)msg;
			}
		
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			closeJMS(producer, consumer, session, conn);
		}
		
		return message;
	}
	
	@Override
	public void closeJMS(MessageProducer producer, MessageConsumer consumer, Session session, Connection conn) {
		
			try {
				if (producer != null) {
					producer.close();
				}
				if (consumer != null) {
					consumer.close();
				}
				if (session != null) {
					session.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		
	}
	
	



	
}
