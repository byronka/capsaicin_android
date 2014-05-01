package com.renomad.capsaicin;

import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.database.Cursor;
import android.content.Intent;
import android.content.Context;
import android.content.ContentResolver;
import android.content.CursorLoader;
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
import java.util.List;
import java.lang.Void;
import android.os.Build;


public class GeneralActivity extends ActionBarActivity {

    ViewPager viewPager;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableConnectionReuseIfNecessary();
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

    public void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
   
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (hasVideoAvailable()) {
            goToVideoRecorder();
        } else {
            DialogHelper.showGenericDialog("Cannot record video; no capability on device", this);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hasVideoAvailable() {
        /** Check if this device has a camera */
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void goToVideoRecorder() {
        Intent intent = new Intent(this, RecordVideoActivity.class);
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            Log.d("GeneralActivity", "data object was null in onActivityResult");
            return;
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Log.i("GeneralActivity", "returned data is " + data.getStringExtra("com.renomad.capsaicin.fileuri"));
            File videoFile = new File(data.getStringExtra("com.renomad.capsaicin.fileuri"));
            new UploadFilesTask().execute(videoFile);
        }
    }

    /**
     *   TODO - BK - 3/10/2014 - need to add a progress bar dialog for uploading?
     */
    private class UploadFilesTask extends AsyncTask<File, Integer, Long> {
        protected Long doInBackground(File... results) {
            int count = results.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                new IoHelper().uploadToServer(results[i]);
                if (!results[i].delete()) {
                    Log.e("GeneralActivity", "delete failed on the file we just sent");
                }
            }
            return totalSize;
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
