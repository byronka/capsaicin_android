package com.renomad.capsaicin.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.app.Instrumentation;
import android.widget.EditText;
import com.renomad.capsaicin.GeneralActivity;
import com.renomad.capsaicin.VideoDataProvider;
import com.renomad.capsaicin.R;
import android.util.Log;
import java.io.File;

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
		File file = generalActivity.getCacheDir();
		VideoDataProvider vdp = new VideoDataProvider();
		vdp.controlVideoInTrans(
				generalActivity.getBaseContext(), (byte)5);
		mInstrumentation.waitForIdleSync();
	}

}
