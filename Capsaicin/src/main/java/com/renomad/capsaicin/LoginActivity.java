package com.renomad.capsaicin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final View go_button = findViewById(R.id.login_go_button);
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent();
                final ComponentName generalActivity = new ComponentName("com.renomad.capsaicin", "com.renomad.capsaicin.GeneralActivity");
                intent.setComponent(generalActivity);
                startActivity(intent);
            }
        });
    }
}
