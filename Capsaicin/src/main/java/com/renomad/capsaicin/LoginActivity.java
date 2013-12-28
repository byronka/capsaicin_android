package com.renomad.capsaicin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final View go_button = findViewById(R.id.login_go_button);
        go_button.setOnClickListener(new GoButtonHandler());
    }

	class GoButtonHandler implements View.OnClickListener {

			@Override
			public void onClick(View view) {
				if (loginFieldsFilled(
							getUsernameText(), 
							getPasswordText())) {
					showEmptyFieldValidationToast();
					return;
				}

				final Intent intent = new Intent();
				final ComponentName generalActivity = 
					new ComponentName("com.renomad.capsaicin", 
						"com.renomad.capsaicin.GeneralActivity");
				intent.setComponent(generalActivity);
				startActivity(intent);
			}
	}

	public void showEmptyFieldValidationToast() {
		Toast.makeText(getApplicationContext(), 
				"this is my Toast message!!! =)",
			Toast.LENGTH_LONG).show();
	}

	public String getUsernameText() {
		EditText username = (EditText)findViewById(R.id.username);
		String usernameString = username.getText().toString();
		return usernameString;
	}

	public String getPasswordText() {
		EditText password = (EditText)findViewById(R.id.password);
		String passwordString = password.getText().toString();
		return passwordString;
	}

	public boolean loginFieldsFilled(String usernameString, 
			String passwordString) {
		return (!(usernameString.isEmpty() || passwordString.isEmpty()));
	}
}
