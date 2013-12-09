package com.renomad.capsaicin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class UniqueVideoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater li, ViewGroup vg, Bundle bundle) {
        View rootView = li.inflate(R.layout.fragment_unique_video, vg, false);
        return rootView;
    }
}
