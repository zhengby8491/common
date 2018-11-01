package com.huayin.common.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

//网易新注册的邮箱不提供SMTP和POP服务
public class Sender
{

	private Properties props;

	private Authenticator auth;

	public Sender(Properties props)
	{
		this.props = props;
		auth = new MailAuthenticator(props.getProperty("mail.from"), props.getProperty("mail.from.password"));
	}

	public void send(String sendto, String subject, String content)
	{
		try
		{
			Session session = Session.getInstance(props, auth);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(props.getProperty("mail.from")));
			InternetAddress[] tos = InternetAddress.parse(sendto);
			msg.setRecipients(Message.RecipientType.TO, tos);
			msg.setSubject(subject);
			msg.setText(content);
			Transport.send(msg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 *  邮件单发
	 * </pre>
	 * @param sendto 邮件接收人地址
	 * @param subject 主题
	 * @param html 网页内容
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void sendHtmlBase64(String sendto, String subject, String html) throws AddressException, MessagingException,
			UnsupportedEncodingException
	{
		Session session = Session.getInstance(props, auth);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(props.getProperty("mail.from"), props.getProperty("mail.username"), "gbk"));
		InternetAddress[] tos = InternetAddress.parse(sendto);
		msg.setRecipients(Message.RecipientType.TO, tos);
		msg.setSubject(MimeUtility.encodeText(subject, "gbk", "B"));
		msg.setHeader("Content-Type", "text/html; charset=gbk");

		MimeBodyPart body = new MimeBodyPart();
		body.setContent(html, "text/html; charset=gbk");
		body.setHeader("Content-Transfer-Encoding", "base64");
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(body);

		msg.setContent(mp);

		Transport transport = session.getTransport("smtp");
		transport.connect();
		transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

	public void sendHtmlText(String sendto, String subject, String html) throws AddressException, MessagingException,
			UnsupportedEncodingException
	{
		Session session = Session.getInstance(props, auth);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(props.getProperty("mail.from")));
		InternetAddress[] tos = InternetAddress.parse(sendto);
		msg.setRecipients(Message.RecipientType.TO, tos);
		msg.setSubject(MimeUtility.encodeText(subject, "gbk", "B"));
		msg.setHeader("Content-Type", "text/html; charset=gbk");
		msg.setContent(html, "text/html;charset=gbk");
		Transport transport = session.getTransport("smtp");
		transport.connect();
		transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

	public void sendHtmlFile(String sendto, String subject, String filename) throws IOException, AddressException,
			MessagingException
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String line = br.readLine();
		while (line != null)
		{
			sb.append(line).append("\r\n");
			line = br.readLine();
		}
		br.close();
		sendHtmlText(sendto, subject, sb.toString());
	}

	public void sendHtmlTest(String sendto, String subject, String html)
	{
		try
		{
			Session session = Session.getInstance(props, auth);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(props.getProperty("mail.from")));
			InternetAddress[] tos = InternetAddress.parse(sendto);
			msg.setRecipients(Message.RecipientType.TO, tos);
			msg.setSubject(MimeUtility.encodeText(subject, "gb2312", "B"));
			msg.setHeader("Content-Type", "text/html; charset=gb2312");

			MimeBodyPart body = new MimeBodyPart();
			body.setContent(html, "text/html; charset=gb2312");
			body.setHeader("Content-Transfer-Encoding", "base64");
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(body);

			msg.setContent(mp);

			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
			transport.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean checkEmail(String mail)
	{
		String regex = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2}|net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|name|NAME)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mail);
		return m.find();
	}

	/**
	 * <pre>
	  * 邮件群发
	  * </pre>
	 * @param contacts 联系人列表
	 * @param subject 主题
	 * @param html 网页内容
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void sendMailList(List<Contact> contacts, String subject, String html) throws UnsupportedEncodingException,
			MessagingException
	{
		Session session = Session.getInstance(props, auth);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(props.getProperty("mail.from"), props.getProperty("mail.username"), "gbk"));
		msg.setRecipients(Message.RecipientType.TO, this.assembleReceipt(contacts));
		msg.setSubject(MimeUtility.encodeText(subject, "gbk", "B"));
		msg.setHeader("Content-Type", "text/html; charset=gbk");

		MimeBodyPart body = new MimeBodyPart();
		body.setContent(html, "text/html; charset=gbk");
		body.setHeader("Content-Transfer-Encoding", "base64");
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(body);

		msg.setContent(mp);

		Transport transport = session.getTransport("smtp");
		transport.connect();
		transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
		transport.close();
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

	public static void main(String[] args) throws Exception
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", "203.86.24.9");
		props.put("mail.smtp.auth", "true");
		props.put("mail.from", "pengfangliang@szhelper.com");
		props.put("mail.from.password", "1314920");
		Sender sender = new Sender(props);
		String to = "pfl1984@126.com";
		String subject = "JavaMail Html Base64 mail 邮件测试主题";
		StringBuffer content = new StringBuffer();
		content.append("<html><head></head><body>")
				.append("<h1 style=\"color:#090;font-family:tahoma;\">JavaMail Html Base64 mail 邮件测试主题</h1>")
				.append("</body></html>");
		sender.send(to, subject, content.toString());
		System.out.println("Send mail OK. " + to);
	}
}
