package com.renomad.capsaicin.tests;

import android.test.ActivityInstrumentationTestCase2;
import com.renomad.capsaicin.LoginActivity;
import com.renomad.capsaicin.R;
import android.widget.EditText;

public class LoginActivity_tests 
	extends ActivityInstrumentationTestCase2<LoginActivity> {

	private LoginActivity loginActivity;

	public LoginActivity_tests() {
		super("com.renomad.capsaicin", LoginActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		loginActivity = getActivity();
	}

	public void testPasswordField() {
		final EditText password = (EditText) loginActivity.findViewById(R.id.password);		
		loginActivity.runOnUiThread(new Runnable() {
			public void run() {
				password.setText("this is a username");
				String passwordText = loginActivity.getPasswordText();
				assertEquals("this is a username", passwordText);
			}
		});
	}

	public void testUsernameField() {
		final EditText username = (EditText) loginActivity.findViewById(R.id.username);
		loginActivity.runOnUiThread(new Runnable() {
			public void run() {
				username.setText("this is a username");
				String usernameText = loginActivity.getUsernameText();
				assertEquals("this is a username", usernameText);
			}
		});
	}

	public void testLoginFieldsFilled_happyCase() {
		String username = "a username";
		String password = "a password";
		boolean isFilled = loginActivity
			.loginFieldsFilled(username, password);
		assertTrue(isFilled);
	}
	public void testLoginFieldsFilled_negativeCaseUsername() {
		String username = "";
		String password = "a password";
		boolean isFilled = loginActivity
			.loginFieldsFilled(username, password);
		assertFalse(isFilled);
	}
	public void testLoginFieldsFilled_negativeCasePassword() {
		String username = "a username";
		String password = "";
		boolean isFilled = loginActivity
			.loginFieldsFilled(username, password);
		assertFalse(isFilled);
	}
		

}
