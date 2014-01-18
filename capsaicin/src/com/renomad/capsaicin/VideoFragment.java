package com.renomad.capsaicin;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.VideoView;


public class VideoFragment extends ListFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		VideoAdapter myVideoAdapter = new VideoAdapter(view, savedInstanceState);
		myVideoAdapter.instantiateFakeVideos();
		setListAdapter(myVideoAdapter);
	}
}
