package com.renomad.capsaicin;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListAdapter;

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
            final View myVideoView = li.inflate(R.layout.video_view, parent, false);
            wireUpTheVideoView(myVideoView);
            return myVideoView;
        }
        return view;
        //nothing happens here for now.  TODO - BK - actually wire up data here.
    }

    private void wireUpTheVideoView(View myVideoView) {
        Button lines_button = (Button)myVideoView.findViewById(R.id.lines_button);
        lines_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("VideoAdapter.java", "you just clicked me!");
            }
        });
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
