package com.renomad.capsaicin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;


public class VideoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        final VideoView videoView;
        View tempVideoView = findViewById(R.id.videoView1);
        if (tempVideoView != null) {
            videoView = (VideoView) tempVideoView;
            videoView.setVideoPath("http://192.168.1.103:8080/byron_talking.mp4");
            videoView.start();
        }
    }
}
