package com.renomad.capsaicin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Byron on 12/7/13.
 */
public class VideoAdapter implements ListAdapter {

    private VideoItem[] videoItems;
    private int itemCount = 0;

    private View view;
    private Bundle bundle;

   public VideoAdapter(View view, Bundle bundle) {
        this.view = view;
        this.bundle = bundle;
   }

    public void instantiateFakeVideos() {
       videoItems = new VideoItem[3];
        videoItems[0] = VideoItem.createVideoItem(0, "http://172.60.12.13:8080/byron_talking.mp4");
        videoItems[1] = VideoItem.createVideoItem(1, "http://172.60.12.13:8080/byron_talking.mp4");
        videoItems[2] = VideoItem.createVideoItem(2, "http://172.60.12.13:8080/byron_talking.mp4");
        itemCount = 3;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        //do nothing for now
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        //do nothing for now
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Override
    public Object getItem(int i) {
        return videoItems[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (parent != null) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            final View my_video_item_view = li.inflate(R.layout.video_item_view, parent, false);
            wireUpTheVideoView(my_video_item_view);
            return my_video_item_view;
        }
        return view;
    }

    private void wireUpTheVideoView(View myVideoItemView) {

        final Context myContext = myVideoItemView.getContext();
        Button lines_button = (Button)myVideoItemView.findViewById(R.id.video_comment_button);
        lines_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent();
                final ComponentName generalActivity = new ComponentName("com.renomad.capsaicin",
                        "com.renomad.capsaicin.UniqueVideoActivity");
                intent.setComponent(generalActivity);
                myContext.startActivity(intent);
            }
        });

        final VideoView videoView = (VideoView)myVideoItemView.findViewById(R.id.videoView);
        if (videoView != null) {

            final MediaController myMediaController = new MediaController(videoView.getContext());
            final ImageView pictureView = (ImageView)myVideoItemView.findViewById(R.id.my_fake_video);
            if (pictureView != null) {

                videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!view.isFocused()) {

                            videoView.setVisibility(View.GONE);
                            pictureView.setVisibility(View.VISIBLE);
                        }
                    }
                });

                pictureView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("VideoFragment", "You just clicked the picture!");
                        pictureView.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        String url = "http://192.168.1.6:8080/byron_talking.mp4"; // your URL here
                        videoView.setVideoPath(url);
                        videoView.setMediaController(myMediaController);
                        videoView.start();
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int i) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        //we will only return one type of view - the video view
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return videoItems.length == 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }
}