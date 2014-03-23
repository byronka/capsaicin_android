package com.renomad.capsaicin;

import android.app.AlertDialog;
import android.content.Context;

public class DialogHelper {

    public void showGenericDialog(String text, Context context) {
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setMessage(text)
	    .setTitle("Alert");
	AlertDialog dialog = builder.create();
	dialog.show();
    }

}
