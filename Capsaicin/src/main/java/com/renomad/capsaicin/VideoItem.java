package com.renomad.capsaicin;

public class VideoItem {
    private final String videoUrl;
    private final int videoId;

    private VideoItem(int id, String url) {
        this.videoId = id;
        this.videoUrl = url;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getVideoId() {
        return videoId;
    }

    public static VideoItem createVideoItem(int i, String s) {
       VideoItem vi = new VideoItem(i, s);
        return vi;
    }
}
