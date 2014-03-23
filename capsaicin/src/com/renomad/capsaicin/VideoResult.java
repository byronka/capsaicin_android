package com.renomad.capsaicin;

public class VideoResult {
    private final String url;
    private final int videoSize;

    public String getUrl() {
	return url;
    }

    public int getVideoSize() {
	return videoSize;
    }

    public VideoResult(String url, int videoSize) {
	this.url = url;
	this.videoSize = videoSize;
    }
}
