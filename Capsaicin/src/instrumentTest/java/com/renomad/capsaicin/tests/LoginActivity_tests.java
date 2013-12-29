package com.renomad.capsaicin.tests;

import android.test.ActivityInstrumentationTestCase2;
import com.renomad.capsaicin.LoginActivity;
import com.renomad.capsaicin.R;
import android.widget.EditText;
import android.view.View;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityResult;
import android.app.Instrumentation.ActivityMonitor;

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
		final EditText password = 
			(EditText) loginActivity.findViewById(R.id.password);		
		loginActivity.runOnUiThread(new Runnable() {
			public void run() {
				password.setText("this is a username");
				String passwordText = loginActivity.getPasswordText();
				assertEquals("this is a username", passwordText);
			}
		});
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

	public void testGoButton_happycase() {
		final EditText username =
			(EditText) loginActivity.findViewById(R.id.username);
		final EditText password =
			(EditText) loginActivity.findViewById(R.id.password);
		final View goButton =
			loginActivity.findViewById(R.id.login_go_button);
		final Instrumentation.ActivityResult result = 
			new Instrumentation.ActivityResult(13, null);
		final ActivityMonitor myMonitor =
			new Instrumentation.ActivityMonitor(
					"com.renomad.capsaicin.GeneralActivity", 
					result,
					true);
		loginActivity.runOnUiThread(new Runnable() {
			public void run() {
				username.setText("this is a username");
				password.setText("this is a password");
				goButton.performClick();
				int numOfHitsOnGeneralActivity = myMonitor.getHits();
				assertTrue(numOfHitsOnGeneralActivity > 0);
			}
		});

	}

	public void testGoButton_negativeCase_password() {
		final EditText username =
			(EditText) loginActivity.findViewById(R.id.username);
		final EditText password =
			(EditText) loginActivity.findViewById(R.id.password);
		final View goButton =
			loginActivity.findViewById(R.id.login_go_button);
		final Instrumentation.ActivityResult result = 
			new Instrumentation.ActivityResult(13, null);
		final ActivityMonitor myMonitor =
			new Instrumentation.ActivityMonitor(
					"com.renomad.capsaicin.GeneralActivity", 
					result,
					true);
		loginActivity.runOnUiThread(new Runnable() {
			public void run() {
				username.setText("this is a username");
				password.setText("");
				goButton.performClick();
				int numOfHitsOnGeneralActivity = myMonitor.getHits();
				assertEquals(0, numOfHitsOnGeneralActivity);
			}
		});
	}

	public void testGoButton_negativeCase_username() {
		final EditText username =
			(EditText) loginActivity.findViewById(R.id.username);
		final EditText password =
			(EditText) loginActivity.findViewById(R.id.password);
		final View goButton =
			loginActivity.findViewById(R.id.login_go_button);
		final Instrumentation.ActivityResult result = 
			new Instrumentation.ActivityResult(13, null);
		final ActivityMonitor myMonitor =
			new Instrumentation.ActivityMonitor(
					"com.renomad.capsaicin.GeneralActivity", 
					result,
					true);
		loginActivity.runOnUiThread(new Runnable() {
			public void run() {
				username.setText("");
				password.setText("this is a password");
				goButton.performClick();
				int numOfHitsOnGeneralActivity = myMonitor.getHits();
				assertEquals(0, numOfHitsOnGeneralActivity);
			}
		});
	}
		

}
