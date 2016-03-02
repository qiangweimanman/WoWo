package com.foodshop.admin.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * 基于ActiveMQ的JMS封装类
 * 
 * @author 木小草
 *
 */
public interface AdminJMS {
	
	/**
	 * 发送Text的消息，method指定消息发送方式
	 * @param msgContext
	 * @param method true：queue flase：topic
	 * @return
	 */
	public abstract boolean sendTextMessage(String msgContext,boolean method);
	
	/**
	 * 接收Text消息，method指定接收方式
	 * @param method method true：queue flase：topic
	 * @return
	 */
	public abstract String receiveTextMessage(boolean method);
	
	/**
	 * 发送Map消息，method指定接收方式
	 * @param mapContext
	 * @param method method true：queue flase：topic
	 * @return
	 */
	public abstract boolean sendMapMessage(MapMessage mapContext,boolean method);
	
	/**
	 * 接收Map的消息，method指定消息发送方式
	 * @param method true：queue flase：topic
	 * @return
	 */
	public abstract MapMessage receiveMapMessage(boolean method);
	
	/**
	 * 发送Object的消息，method指定消息发送方式
	 * @param objectContext
	 * @param method true：queue flase：topic
	 * @return
	 * @throws JMSException 
	 */
	public abstract boolean sendObjectMessage(Object objectContext,boolean method);
	
	/**
	 * 接收Object的消息，method指定消息发送方式
	 * @param method true：queue flase：topic
	 * @return
	 */
	public abstract ObjectMessage receiveObjectMessage(boolean method);
	
	/**
	 * 结束处理
	 * @param producer
	 * @param consumer
	 * @param session
	 * @param conn
	 */
	public abstract void closeJMS(MessageProducer producer,MessageConsumer consumer,Session session,Connection conn);
}
