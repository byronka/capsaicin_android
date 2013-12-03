package com.renomad.capsaicin;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import java.util.zip.Inflater;


public class VideoFragment extends Fragment {

    @Override
    protected void onCreateView(LayoutInflater li, ViewGroup vg, Bundle bundle) {
        return Inflater.inflate(R.layout.fragment_videos, false);
        final VideoView videoView;
        View tempVideoView = findViewById(R.id.videoView1);
        if (tempVideoView != null) {
            videoView = (VideoView) tempVideoView;
            videoView.setVideoPath("http://192.168.1.6:8080/byron_talking.mp4");
            videoView.start();
        }
    }
}
