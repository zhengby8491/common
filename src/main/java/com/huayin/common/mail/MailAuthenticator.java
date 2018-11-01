package com.huayin.common.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator
{
	private String userName = null;

	private String password = null;

	public MailAuthenticator(String user, String pass)
	{
		userName = user;
		password = pass;
	}

	public PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(userName, password);
	}

}
