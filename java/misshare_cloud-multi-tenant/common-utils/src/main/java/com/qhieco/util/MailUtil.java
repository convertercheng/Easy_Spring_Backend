package com.qhieco.util;

import java.io.File;
import java.security.Security;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author myz
 * @date 2017年12月29日 下午 15:08
 * 
 */
public class MailUtil {

    private static final String MAIL_HOST = "mail.host";
    private static final String MAIL_AUTH = "mail.smtp.auth";
    private static final String MAIL_HOST_VALUE = "smtp.qhiehome.com";
    private static final String MAIL_AUTH_VALUE = "true";

    private static final String AUTHENTICATION_INVOICE_MAIL_ACCOUNT_PWD = "Qhie2018fp";

    private static final String INVOICE_MAIL_FROM = "xnl-invoice@qhieco.com";
    private static final String MAIL_TO = "qhiecw@qhieco.com";
    private static final String MAIL_CC = "zhaoxiang@qhiehome.com";

    private static final String MAIL_SUBJECT = "享你了——微信商户或支付宝平台问题";
    private static final String FILE_NAME = "电子发票.pdf";
    private static final String MAIL_TEMPLATE = "财务部的同事你们好：<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;请注意，微信商户或支付宝平台出现问题: <font size='4' color='red'>%s</font>。";
    private static final String MAIL_ENCODING = "text/html;charset=UTF-8";

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Session mSession;

    private MailUtil(){
    	Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", MAIL_HOST_VALUE);
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty(MAIL_AUTH, MAIL_AUTH_VALUE);
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(INVOICE_MAIL_FROM, AUTHENTICATION_INVOICE_MAIL_ACCOUNT_PWD);
            }
        };
        mSession =  Session.getDefaultInstance(props, authenticator);
    }

    private static final class MailUtilHolder {
        private static final MailUtil INSTANCE = new MailUtil();
    }

    public static MailUtil getInstance() {
        return MailUtilHolder.INSTANCE;
    }

    public void sendEmail(String content) {
        try {
            Message message = new MimeMessage(mSession);
            message.setFrom(new InternetAddress(INVOICE_MAIL_FROM));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(MAIL_TO));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(MAIL_CC));
            message.setSubject(MAIL_SUBJECT);
            String mailContent = String.format(Locale.CHINA, MAIL_TEMPLATE, content);
            message.setContent(mailContent, MAIL_ENCODING);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /** 
     * 发送邮件 
     *  
     * @param recipient 
     *            收件人邮箱地址 
     * @param subject 
     *            邮件主题 
     * @param content 
     *            邮件内容 
     * @param file 
     *            附件 
     * @throws Exception 
     */  
    public void send(String recipient, String subject, String content, File file) {  
        try {
        	log.info("开始调用send方法recipient:{},subject:{}",recipient,subject);
		    // 创建mime类型邮件  
		    final MimeMessage message = new MimeMessage(mSession);  
		    // 设置发信人  
		    message.setFrom(new InternetAddress(INVOICE_MAIL_FROM));  
		    // 设置收件人  
		    message.setRecipient(Message.RecipientType.TO, new InternetAddress(  
		            recipient));  
		    // 设置主题  
		    message.setSubject(subject);  
		    // 设置邮件内容  
		    if (null == file) {  
		        message.setContent(content, MAIL_ENCODING);  
		    } else {  
		    	log.info("有附件 .");
		        //创建 Mimemultipart添加内容(可包含多个附件)  
		        MimeMultipart multipart = new MimeMultipart();  
		        //MimeBodyPart(用于信件内容/附件)  
		        BodyPart bodyPart = new MimeBodyPart();  
		        bodyPart.setContent(content, MAIL_ENCODING);  
		        //添加到MimeMultipart对象中  
		        multipart.addBodyPart(bodyPart);  
		        //创建FileDAtaSource(用于添加附件)  
		        FileDataSource fds = new FileDataSource(file);  
		        BodyPart fileBodyPart = new MimeBodyPart();  
		        log.info("字符流形式装入文件  .");
		        // 字符流形式装入文件  
		        fileBodyPart.setDataHandler(new DataHandler(fds));  
		        // 设置附件文件名  
		        fileBodyPart.setFileName(MimeUtility.encodeText(FILE_NAME));  
		        multipart.addBodyPart(fileBodyPart);  
		        log.info("message.setContent(multipart).");
		        message.setContent(multipart);  
		    }  
		    // 设置发信时间  
		    //message.setSentDate(new Date());  
		    log.info("存储邮件信息  .");
		    // 存储邮件信息  
		    message.saveChanges();  
		    //message.setFileName(filename)  
		    log.info("调用Transport.send方法.");
		    // 发送邮件  
		    Transport transport = mSession.getTransport("smtp");
		    transport.send(message);  
		    log.info("发送邮件成功.");
        }catch (Exception e) {
        	log.error("system error"+e);
        	e.printStackTrace();
		}
    }  

    public static void main(String[] args) {
        MailUtil.getInstance().sendEmail("商户可用退款余额不足");
    }

}

