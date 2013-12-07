package com.renomad.capsaicin;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.VideoView;


public class VideoFragment extends ListFragment {

    private VideoView videoView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        VideoAdapter myVideoAdapter = new VideoAdapter(view, savedInstanceState);
        myVideoAdapter.instantiateFakeVideos();
        setListAdapter(myVideoAdapter);
    }
//    @Override
//    public View onCreateView(LayoutInflater li, ViewGroup vg, Bundle bundle) {
//        super.onCreateView(li, vg, bundle);
//        VideoAdapter myVideoAdapter = new VideoAdapter(li, vg, bundle);
//        myVideoAdapter.instantiateFakeVideos();
//        setListAdapter(myVideoAdapter);

//        View tempVideoView = rootView.findViewById(R.id.videoView1);
//        if (tempVideoView != null) {
//            videoView = (VideoView) tempVideoView;
//            videoView.setClickable(true);
//            videoView.setOnClickListener(this);
//            videoView.setVideoPath("http://172.31.98.89:8080/byron_talking.mp4");
//        }

//        return vg;
//    }

//    @Override
//    public void onClick(View view) {
//           VideoView myVideoView = (VideoView) view;
//        if (myVideoView.isPlaying()) {
//            videoView.pause();
//        } else {
//            videoView.start();
//        }
//    }


}
