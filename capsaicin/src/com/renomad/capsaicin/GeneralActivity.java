package com.renomad.capsaicin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;


public class GeneralActivity extends ActionBarActivity {

    ViewPager viewPager;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

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
        viewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
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
