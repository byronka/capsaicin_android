package com.renomad.capsaicin;

import android.os.Bundle;
import android.app.Activity;

public class DevSetupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_devsetup);
	loadFieldsOnPage();
	wireUpSaveButton();

    }

    private void wireUpSaveButton() {
	final Button saveButton = (Button) findViewById(R.id.save_button);
	saveButton.setOnClickListener(new OnClickListener(){
		saveFieldsToDatabase();
	    });
    }

    private void loadFieldsOnPage() {
	EditText server_url_edittext = (EditText) findViewById(R.id.server_url);
	CharSequence urltext = getSavedUrl();
	server_url_edittext.setText(urltext);
    }

    private void saveFieldsToDatabase() {
	EditText server_url_edittext = (EditText) findViewById(R.id.server_url);
	CharSequence urltext = server_url_edittext.getText();
	Data.save
    }
