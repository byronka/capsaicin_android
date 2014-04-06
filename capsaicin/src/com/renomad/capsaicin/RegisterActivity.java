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
        EditText username = (EditText)findViewById(R.id.registerusername);
        EditText password = (EditText)findViewById(R.id.registerpassword);
        String usernameText = username.getText();
        String passwordText = password.getText();
    }

    private class SaveRegistrationTask extends AsyncTask<Void, Void, List<String>> {

        ProgressDialog progressDialog;
        private Context mContext;
        private String mRegistration;

        SaveRegistrationTask(Context context, String registration) {
            mContext = context;
            mRegistration = registration;
        }

        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        }

        protected void onPostExecute(List<String> results) {
            progressDialog.dismiss();
	    Log.i("SaveRegistrationTask", 
                  "result from saving registration was:");
        }

        protected List<String> doInBackground(Void... params) {
            try{
                return new IoHelper().registerUser(mRegistration);
            } catch (Exception e) {
                new DialogHelper().showGenericDialog("Network error while sending registration", mContext);
            }
            return new ArrayList<String>();
        }
    }

}
