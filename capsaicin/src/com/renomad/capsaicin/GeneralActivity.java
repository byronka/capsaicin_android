package com.renomad.capsaicin;

import android.database.Cursor;
import android.content.Intent;
import android.content.ContentResolver;
import android.provider.MediaStore;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

public class GeneralActivity extends ActionBarActivity {

    ViewPager viewPager;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final String SERVER_URL = "http://192.168.56.2/test.py";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.activity_general_viewpager);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
		    actionBar.setSelectedNavigationItem(position);
		}
	    });
        viewPager.setAdapter(mAppSectionsPagerAdapter);

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
	    actionBar.addTab(actionBar.newTab()
			     .setText(mAppSectionsPagerAdapter.getPageTitle(i))
			     .setTabListener(new GeneralActivityTabListener(viewPager)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.record_video:
		dispatchTakeVideoIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakeVideoIntent() {
	Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
	    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
	}
    }

    private class VideoResult {
	private final String url;
	private final int videoSize;

	public String getUrl() {
	    return url;
	}

	public int getVideoSize() {
	    return videoSize;
	}

	public VideoResult(String url, int videoSize) {
	    this.url = url;
	    this.videoSize = videoSize;
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
	    Uri videoUri = data.getData();
	    Log.i("onActivityResult", "got video back from camera at uri: " + videoUri);
	    String videoPath = getRealPathFromURI(videoUri);
	    int videoSize = getSizeOfVideo(videoUri);
	    VideoResult vResult = new VideoResult(videoPath, videoSize);
	    new UploadFilesTask().execute(vResult);
	}
    }

    private int getSizeOfVideo(Uri videoUri) {
	//TODO THIS IS FALSE!! FIX ME WHEn YOU CAN
	return 1000;
    }

    public String getRealPathFromURI(Uri contentUri) {
	String[] proj = { MediaStore.Images.Media.DATA };
	Cursor cursor = managedQuery(contentUri, proj, null, null, null);
	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	cursor.moveToFirst();
	return cursor.getString(column_index);
    }

    /**
     *   TODO - BK - 3/10/2014 - need to add a progress bar dialog for uploading?
     */
    private class UploadFilesTask extends AsyncTask<VideoResult, Integer, Long> {
	protected Long doInBackground(VideoResult... results) {
	    int count = results.length;
	    long totalSize = 0;
	    for (int i = 0; i < count; i++) {
		totalSize += uploadToServer(results[i]);
		publishProgress((int) ((i / (float) count) * 100));
		// Escape early if cancel() is called
		//		if (isCancelled()) break;
	    }
	    return totalSize;
	}

	protected void onProgressUpdate(Integer... progress) {
	    //	    setProgressPercent(progress[0]);
	}

	protected void onPostExecute(Long result) {
	    //	    showDialog("Uploaded " + result + " bytes");
	}
    }

    private static int writeStream(OutputStream out) {
	//TODO send out the stream!
	return 20; // TODO totally fake!
    }

    private static int readStream(InputStream in) {
	//TODO read in the stream!
	return 20; //TODO totally fake!
    }

    private static int uploadToServer(VideoResult result) {
	HttpURLConnection urlConnection = null;
	try {
	    URL url = new URL(result.getUrl());
	    urlConnection = (HttpURLConnection) url.openConnection();
	    urlConnection.setDoOutput(true);
	    urlConnection.setFixedLengthStreamingMode(result.getVideoSize());

	    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
	    int bytesWritten = 0;
	    bytesWritten = writeStream(out);

	    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
	    readStream(in);
	    return bytesWritten;
	}
	catch (Exception e) {
	    return 0;
	}
	finally {
	    if (urlConnection != null) {
		urlConnection.disconnect();
	    }
	}
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment 
     * corresponding to one of the primary
     * sections of the app.
     */
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

	VideoFragment vFrag1 = new VideoFragment();
	VideoFragment vFrag2 = new VideoFragment();
	VideoFragment vFrag3 = new VideoFragment();
	ProfileFragment pFrag = new ProfileFragment();

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
	    case 0:
		return vFrag1;
	    case 1:
		return vFrag2;
	    case 2:
		return vFrag3;
	    case 3:
		return pFrag;
	    default:
		return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
	    case 0:
		return "Most Recent";
	    case 1:
		return "Most Popular";
	    case 2:
		return "Video of the day";
	    case 3:
		return "Profile";

            }
            return "Empty - shouldn't get here";
        }
    }
}
