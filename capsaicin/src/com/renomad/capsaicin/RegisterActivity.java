package com.renomad.capsaicin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final View register_button = findViewById(R.id.register_button);
        register_button.setOnClickListener(new RegisterButtonHandler());
    }

    class RegisterButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            saveRegistrationInformation(view);
            final Intent intent = new Intent();
            final ComponentName loginActivity = 
                new ComponentName("com.renomad.capsaicin", 
                                  "com.renomad.capsaicin.LoginActivity");
            intent.setComponent(loginActivity);
            startActivity(intent);
        }
    }

    private void saveRegistrationInformation(View view) {
        //get field values and send them to capsaicintesting.net
    }

}
