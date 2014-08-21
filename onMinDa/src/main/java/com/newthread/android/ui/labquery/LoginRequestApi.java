package com.newthread.android.ui.labquery;

public class LoginRequestApi {
	private String Account; // 登陆的账户
	private String Password;// 登陆的密码

	public LoginRequestApi(String account, String password) {
		super();
		Account = account;
		Password = password;
	}

	public String getAccount() {
		return Account;
	}

	public String getPassword() {
		return Password;
	}

}
