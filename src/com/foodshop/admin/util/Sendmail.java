package com.foodshop.admin.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.foodshop.admin.vo.Admin;

/**
* @ClassName: Sendmail
* @Description: Sendmail类继承Thread，因此Sendmail就是一个线程类，这个线程类用于给指定的用户发送Email
* @author: 木小草
*
*/ 
public class Sendmail extends Thread {
    //用于给用户发送邮件的邮箱
    private String from = "qiangweimanman@163.com";
    //邮箱的用户名
    private String username = "qiangweimanman";
    //邮箱的密码
    private String password = "ysm19910225";
    //发送邮件的服务器地址
    private String host = "smtp.163.com";
    
    private Admin admin;
    public Sendmail(Admin admin){
        this.admin = admin;
    }
    
    /* 重写run方法的实现，在run方法中发送邮件给指定的用户
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        try{
            Properties prop = new Properties();
            prop.setProperty("mail.host", host);
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");
            Session session = Session.getInstance(prop);
            session.setDebug(true);
            Transport ts = session.getTransport();
            ts.connect(host, username, password);
            Message message = createEmail(session,admin);
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
    * @Method: createEmail
    * @Description: 创建要发送的邮件
    * @Anthor:木小草
    *
    * @param session
    * @param user
    * @return
    * @throws Exception
    */ 
    public Message createEmail(Session session,Admin admin) throws Exception{
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(admin.getAdmin_mail()));
        message.setSubject("用户激活邮件");
        
        String info = "恭喜您注册成功，您的用户名：" + admin.getAdmin_uname() + ",您的密码：" + admin.getAdmin_pwd() + "，请妥善保管，如有问题请联系网站客服!!";
        message.setContent(info, "text/html;charset=UTF-8");
        message.saveChanges();
        return message;
    }
}