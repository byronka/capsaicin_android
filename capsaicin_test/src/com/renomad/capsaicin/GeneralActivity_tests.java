package com.renomad.capsaicin.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.app.Instrumentation;
import android.widget.EditText;
import com.renomad.capsaicin.GeneralActivity;
import com.renomad.capsaicin.R;
import android.util.Log;
import java.io.File;
import java.lang.Thread;
import android.widget.ImageView;

public class GeneralActivity_tests 
	extends ActivityInstrumentationTestCase2<GeneralActivity> {

	private GeneralActivity generalActivity;
	private Instrumentation mInstrumentation;

	public GeneralActivity_tests() {
		super(GeneralActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mInstrumentation = getInstrumentation();
		generalActivity = getActivity();
		if (generalActivity == null) {
			Log.d("generalActivity_tests", "generalActivity was null");
		}
	}

	public void testGettingVideo() {
	    final ImageView imageView = (ImageView) 
		generalActivity.findViewById(2131034191);
	    generalActivity.runOnUiThread(new Runnable() {
		    public void run() {
			imageView.performClick();
		    }
		});
	    mInstrumentation.waitForIdleSync();
	}
//	public void testGettingVideo() {
//		File file = generalActivity.getCacheDir();
//		VideoDataProvider vdp = new VideoDataProvider();
//		vdp.controlVideoInTrans(
//				generalActivity.getBaseContext(), (byte)5);
//		mInstrumentation.waitForIdleSync();
//	}

}
