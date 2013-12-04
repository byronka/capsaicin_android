package com.renomad.capsaicin;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;


public class GeneralActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        ActionBar actionBar = getSupportActionBar();
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoaderManager loaderManager = getSupportLoaderManager();
        ViewPager viewPager = (ViewPager) findViewById(R.id.general_activity_frame);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
