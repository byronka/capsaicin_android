package com.renomad.capsaicin;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import android.net.http.AndroidHttpClient;
import org.apache.http.entity.FileEntity;
import java.io.InputStream;
import android.util.Log;
import java.io.File;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import com.renomad.capsaicin.VideoResult;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.renomad.capsaicin.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

public class IoHelper {

    public List<String> registerUser(String registration) throws Exception {
        ArrayList<String> toReturn = new ArrayList<String>();
	AndroidHttpClient http = 
            AndroidHttpClient.newInstance("capsaicinAndroidClient");
	HttpPost post = 
            new HttpPost(Constants.VIDEO_SERVER_URL + "/registeruser");
        HttpEntity entity = new StringEntity(registration);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
	BufferedReader r = null;
        Log.i("registeruser", "registering to: " + Constants.VIDEO_SERVER_URL);
	try {
	    final HttpResponse response = http.execute(post);
            //TODO - BK - 4/6/2014 - finish setting up here.
            //TODO - BK - see C:\Program Files (x86)\Android_sdk\samples\android-19\legacy\SampleSyncAdapter\src\com\example\android\samplesync\client\NetworkUtilities.java for an example of how to do this.
	} catch (Exception e) {
	    Log.e("registeruser", 
                  "exception in registering user: " + e.toString());
	    throw e;
	} finally {
	    if (http != null) {
		http.close();
	    }
            return toReturn;
	}

    }

    public List<String> getListOfVideos() throws Exception {
	AndroidHttpClient http = AndroidHttpClient.newInstance("capsaicinAndroidClient");
	HttpGet get = new HttpGet(Constants.VIDEO_SERVER_URL + "/listofvideos");
	List<String> videoList = new ArrayList<String>();
	BufferedReader r = null;
	try {
	    InputStream is = http.execute(get).getEntity().getContent();
	    r = new BufferedReader(new InputStreamReader(is));
	    String line;
	    while ((line=r.readLine()) != null) {
		videoList.add(line);	 
	    }   
	} catch (Exception e) {
	    Log.e("getListOfVideos", 
                  "exception in getting list of videos: " + e.toString());
	    throw e;
	} finally {
	    try {
		if (r != null) {
		    r.close();
		}
	    } catch (Exception e) {
		Log.e("getListOfVideos", 
                      "Error in closing the stream wrapping"+ 
		      " the result from getting list of videos " + e);
	    }
	    if (http != null) {
		http.close();
	    }
	}
	return videoList;
    }

    public void uploadToServer(VideoResult result) {
	AndroidHttpClient http = 
            AndroidHttpClient.newInstance("capsaicinAndroidClient");
	HttpPost post = 
            new HttpPost(Constants.VIDEO_SERVER_URL + "uploadvideo");
	post.addHeader("VIDEO_NAME", "testname");
	post.setEntity(new FileEntity(new File(result.getUrl()), 
                                      "application/octet-stream"));
	try {
	    HttpResponse response = http.execute(post);
	} catch (Exception e) {
	    Log.e("uploadToServer", "exception in uploading: " + e.toString());
	}
	finally {
	    if (http != null) {
		http.close();
	    }
	}

    }
}

