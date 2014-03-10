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
        mAppSectionsPagerAdapter = 
	    new AppSectionsPagerAdapter(getSupportFragmentManager());
        viewPager = 
	    (ViewPager) findViewById(R.id.activity_general_viewpager);
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

    /**
     * Use this method to instantiate your menu, and add your items to it. You
     * should return true if you have added items to it and want the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate our menu from the resources by using the menu inflater.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This method is called when one of the menu items to selected. These items
     * can be on the Action Bar, the overflow menu, or the standard options menu. You
     * should return true if you handle the selection.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.record_video:
		dispatchTakeVideoIntent();
                // Here we might start a background refresh task
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
	    Uri videoUri = data.getData();
	    Log.i("onActivityResult", "got video back from camera at uri: " + videoUri);
	    String videoPath = getRealPathFromURI(videoUri);
	    new UploadFilesTask().execute(videoPath);

	}
    }

    public String getRealPathFromURI(Uri contentUri) {
	String[] proj = { MediaStore.Images.Media.DATA };
	Cursor cursor = managedQuery(contentUri, proj, null, null, null);
	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	cursor.moveToFirst();
	return cursor.getString(column_index);
    }

    private class UploadFilesTask extends AsyncTask<String, Integer, Long> {
	protected Long doInBackground(String... urls) {
	    int count = urls.length;
	    long totalSize = 0;
	    for (int i = 0; i < count; i++) {
		totalSize += upLoad2Server(urls[i]);
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

    public static int upLoad2Server(String sourceFileUri) {
	// String [] string = sourceFileUri;
	String fileName = sourceFileUri;
	HttpURLConnection conn = null;
	DataOutputStream dos = null;
	DataInputStream inStream = null;
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";
	int bytesRead, bytesAvailable, bufferSize;
	int serverResponseCode = 0;
	byte[] buffer;
	int maxBufferSize = 1 * 1024 * 1024;
	String responseFromServer = "";

	File sourceFile = new File(sourceFileUri);
	if (!sourceFile.isFile()) {
	    Log.e("GeneralActivity", "URI does not point at a valid file");
	    return 0;
	}
	try { // open a URL connection to the server
	    Log.i("upLoad2Server", "about to upload to server");
	    FileInputStream fileInputStream = new FileInputStream(sourceFile);
	    Log.i("upLoad2Server", "SERVER_URL is " + SERVER_URL);
	    URL url = new URL(SERVER_URL);
	    conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
	    conn.setDoInput(true); // Allow Inputs
	    conn.setDoOutput(true); // Allow Outputs
	    conn.setUseCaches(false); // Don't use a Cached Copy
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Connection", "Keep-Alive");
	    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	    conn.setRequestProperty("uploaded_file", fileName);
	    dos = new DataOutputStream(conn.getOutputStream());

	    dos.writeBytes(twoHyphens + boundary + lineEnd);
	    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
	    dos.writeBytes(lineEnd);

	    bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
	    Log.i("GeneralActivity", "Initial .available : " + bytesAvailable);

	    bufferSize = Math.min(bytesAvailable, maxBufferSize);
	    buffer = new byte[bufferSize];

	    // read file and write it into form...
	    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

	    while (bytesRead > 0) {
		dos.write(buffer, 0, bufferSize);
		bytesAvailable = fileInputStream.available();
		bufferSize = Math.min(bytesAvailable, maxBufferSize);
		bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	    }

	    // send multipart form data necesssary after file data...
	    dos.writeBytes(lineEnd);
	    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

	    // Responses from the server (code and message)
	    serverResponseCode = conn.getResponseCode();
	    String serverResponseMessage = conn.getResponseMessage();

	    Log.i("Upload file to server", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
	    // close streams
	    Log.i("Upload file to server", fileName + " File is written");
	    fileInputStream.close();
	    dos.flush();
	    dos.close();
	} catch (MalformedURLException ex) {
	    ex.printStackTrace();
	    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	//this block will give the response of upload link
	try {
	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn
									 .getInputStream()));
	    String line;
	    while ((line = rd.readLine()) != null) {
		Log.i("GeneralActivity", "RES Message: " + line);
	    }
	    rd.close();
	} catch (IOException ioex) {
	    Log.e("GeneralActivity", "error: " + ioex.getMessage(), ioex);
	}
	return serverResponseCode;  // like 200 (Ok)

    } // end upLoad2Server

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment 
     * corresponding to one of the primary
     * sections of the app.
     */
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
	    case 0:
		return new VideoFragment();
	    case 1:
		return new VideoFragment();
	    case 2:
		return new VideoFragment();
	    case 3:
		return new ProfileFragment();
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
