/**
 * <pre>
 * Title: 		Postman.java
 * Author:		zhaojitao
 * Create:	 	2009-1-19 下午06:02:19
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.mail;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * <pre>
 * javax.mail的邮件发送实现
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2009-1-19
 */
public class Postman
{
	private static final String SMTP_MAIL = "smtp";

	private SMTPConfig config;

	public Postman(SMTPConfig config)
	{
		this.config = config;
	}

	/**
	 * <pre>
	 * 发送邮件
	 * </pre>
	 * @param message 邮件消息体
	 * @param contacts 收件人列表
	 * @throws Exception
	 */
	public void send(MailMessage message, List<Contact> contacts) throws Exception
	{
		Session session = this.openSession();
		MimeMessage mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(config.getMailfrom()));
		mimeMessage.setSentDate(new java.util.Date());
		mimeMessage.setSubject(message.getSubject());
		// set TO address
		mimeMessage.setRecipients(RecipientType.TO, this.assembleReceipt(contacts));

		// set message BODY
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(message.getContent(), "text/html;charset=GBK");
		// attach message BODY
		MimeMultipart mimeMultiPart = new MimeMultipart();
		mimeMultiPart.addBodyPart(mimeBodyPart);
		mimeMessage.setContent(mimeMultiPart);

		// send message
		Transport transport = session.getTransport(SMTP_MAIL);
		transport.connect(config.getSmtp(), config.getSendUser(), config.getSendUserPwd());
		transport.sendMessage(mimeMessage, this.assembleReceipt(contacts));
		transport.close();
	}

	private Session openSession()
	{
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.host", config.getSmtp());
		prop.put("mail.smtp.auth", "true");
		return Session.getDefaultInstance(prop, new Authenticator()
		{
			public PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(config.getSendUser(), config.getSendUserPwd());
			}
		});
	}

	private Address[] assembleReceipt(List<Contact> contacts) throws AddressException
	{
		Address[] mailtoAddress = new InternetAddress[contacts.size()];
		for (int i = 0; i < contacts.size(); i++)
		{
			Contact contact = contacts.get(i);
			try
			{
				mailtoAddress[i] = new InternetAddress(contact.getEmail(), contact.getName());
			}
			catch (UnsupportedEncodingException e)
			{
				mailtoAddress[i] = new InternetAddress(contact.getEmail());
			}
		}
		return mailtoAddress;
	}
}
