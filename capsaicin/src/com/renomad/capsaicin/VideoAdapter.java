package com.renomad.capsaicin;

import java.util.ArrayList;
import com.renomad.capsaicin.DialogHelper;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.database.DataSetObservable;
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
import java.util.List;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.widget.ArrayAdapter;

/**
 * Created by Byron on 12/7/13.
 */
public class VideoAdapter extends ArrayAdapter<VideoItem> {

    private VideoItem[] videoItems;
    private int itemCount = 0;

    public VideoAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        new ListOfFilesTask(this, context).execute(); //every time we instantiate the fragment we go to the server
    }

    @Override
        public View getView(int i, View view, ViewGroup parent) {
        if (parent != null) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            final View my_video_item_view = li.inflate(R.layout.video_item_view, parent, false);
            VideoItem vi = videoItems[i];
            wireUpTheVideoView(my_video_item_view, vi);
            return my_video_item_view;
        }
        return view;
    }

    private void wireUpTheVideoView(View myVideoItemView, final VideoItem vi) {
        
        final Context myContext = myVideoItemView.getContext();
        Button lines_button = 
            (Button)myVideoItemView.findViewById(R.id.video_comment_button);
        if (lines_button == null) {
            //TODO - BK - 1/11/2014 this section is for debugging, make better.
            //I added this in solely to figure out why it was crashing when
            //trying to set the click listener on the lines button.
            Log.d("VideoAdapter", "lines_button was null, cannot instantiate");
        }

        lines_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent();
                    final ComponentName generalActivity = 
                        new ComponentName("com.renomad.capsaicin",
                                          "com.renomad.capsaicin.UniqueVideoActivity");
                    intent.setComponent(generalActivity);
                    myContext.startActivity(intent);
                }
            });

        final VideoView videoView = 
            (VideoView)myVideoItemView.findViewById(R.id.videoView);
        if (videoView != null) {
            final ImageView pictureView = 
                (ImageView)myVideoItemView.findViewById(R.id.my_fake_video);
            if (pictureView != null) {

                myVideoItemView.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            Log.i("VideoFragment", 
                                  "You just touched somewhere other than the lines, or picture!, it was id " + view.getId());
                            videoView.stopPlayback();
                            videoView.setMediaController(null);
                            pictureView.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.GONE);
                            return false;
                        }
                    });

                pictureView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("VideoFragment", "You just clicked the picture!, it is at id " + view.getId());
                            pictureView.setVisibility(View.GONE);
                            videoView.setVisibility(View.VISIBLE);
                            String url = vi.getVideoUrl();
                            Log.i("pictureView.setOnClickListener", "url for video was " + url);
                            videoView.setVideoPath(url);
                            videoView.setMediaController(new MediaController(videoView.getContext()));
                            videoView.start();
                        }
                    });
            }
        }
    }

    private class ListOfFilesTask extends AsyncTask<Void, Void, List<String>> {

        ProgressDialog progressDialog;
        private Context mContext;
        private final VideoAdapter mVideoAdapter;

        ListOfFilesTask(VideoAdapter videoAdapter, Context context) {
            mVideoAdapter = videoAdapter;
            mContext = context;
        }

        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        }

        protected void onPostExecute(List<String> results) {
            progressDialog.dismiss();
            videoItems = new VideoItem[results.size()];
            int index = 0;
            for (String v : results) {
                videoItems[index] = VideoItem
                    .createVideoItem(0, mContext.getString(R.string.server_url) +"downloadvideo/"+ v);
                index++;
            }
            mVideoAdapter.addAll(videoItems);
        }

        protected List<String> doInBackground(Void... params) {
            // List<String> vidnames = new IoHelper().getListOfVideos();
            try{
                return new IoHelper().getListOfVideos();
            } catch (Exception e) {
                new DialogHelper().showGenericDialog("Network error while loading videos", mContext);
            }
            return new ArrayList<String>();
        }
    }
}
