package com.renomad.capsaicin.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.renomad.capsaicin.GeneralActivity;
import com.renomad.capsaicin.R;
import com.renomad.capsaicin.VideoDataHandler;

public class VideoDataHandler_tests
	extends ActivityInstrumentationTestCase2<GeneralActivity> {

	private GeneralActivity generalActivity;
    private VideoDataHandler videoHandler;

	public VideoDataHandler_tests() {
		super(GeneralActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
        generalActivity = getActivity();
	}

    public void testReceiveVideo() {

    }
}
