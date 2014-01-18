package com.renomad.capsaicin.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.app.Instrumentation;
import android.widget.EditText;
import com.renomad.capsaicin.LoginActivity;
import com.renomad.capsaicin.R;
import android.util.Log;

public class LoginActivity_tests 
	extends ActivityInstrumentationTestCase2<LoginActivity> {

	private LoginActivity loginActivity;
	private Instrumentation mInstrumentation;

	public LoginActivity_tests() {
		super(LoginActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mInstrumentation = getInstrumentation();
		loginActivity = getActivity();
		if (loginActivity == null) {
			Log.d("LoginActivity_tests", "loginActivity was null");
		}
	}

	public void testPasswordField() {
		final EditText password = 
			(EditText) loginActivity.findViewById(R.id.password);
		loginActivity.runOnUiThread(new Runnable() {
			public void run() {
				password.setText("this is a username");
				String passwordText = loginActivity.getPasswordText();
				assertEquals("this is a username", passwordText);
			}
		});
		mInstrumentation.waitForIdleSync();
	}

	public void testUsernameField() {
		final EditText username =
			(EditText) loginActivity.findViewById(R.id.username);
		loginActivity.runOnUiThread(new Runnable() {
			public void run() {
				username.setText("this is a username");
				String usernameText = loginActivity.getUsernameText();
				assertEquals("this is a username", usernameText);
			}
		});
		mInstrumentation.waitForIdleSync();
	}

	public void testLoginFieldsFilled_happyCase() {
		String username = "a username";
		String password = "a password";
		boolean isFilled = loginActivity
			.loginFieldsFilled(username, password);
		assertTrue(isFilled);
		mInstrumentation.waitForIdleSync();
	}

	public void testLoginFieldsFilled_negativeCaseUsername() {
		String username = "";
		String password = "a password";
		boolean isFilled = loginActivity
			.loginFieldsFilled(username, password);
		assertFalse(isFilled);
		mInstrumentation.waitForIdleSync();
	}

	public void testLoginFieldsFilled_negativeCasePassword() {
		String username = "a username";
		String password = "";
		boolean isFilled = loginActivity
			.loginFieldsFilled(username, password);
		assertFalse(isFilled);
		mInstrumentation.waitForIdleSync();
	}

    public void testLoginFieldsValid_happycase() {
        String username = "testuser";
        String password = "testpass";
        boolean isValid = loginActivity
                .loginFieldsValid(username, password);
        assertTrue(isValid);
		mInstrumentation.waitForIdleSync();
    }

    public void testLoginFieldsValid_negativeUserCase() {
        String username = "not a valid entry";
        String password = "testpass";
        boolean isValid = loginActivity
                .loginFieldsValid(username, password);
        assertTrue(isValid);
		mInstrumentation.waitForIdleSync();
    }

    public void testLoginFieldsValid_negativePassCase() {
        String username = "testuser";
        String password = "not a valid entry";
        boolean isValid = loginActivity
                .loginFieldsValid(username, password);
        assertTrue(isValid);
		mInstrumentation.waitForIdleSync();
    }

}
