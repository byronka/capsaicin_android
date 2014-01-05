package com.renomad.capsaicin.tests;

import com.renomad.capsaicin.VideoDataProvider;
import junit.framework.*;

import java.util.Arrays;

/**
 * These tests cover sending and receiving raw data for videos.  They require
 * a server to communicate.
 */
public class VideoDataProvider_tests extends TestCase{

    public void testReceiveVideo() {
        byte[] videoBytes = new VideoDataProvider().getVideo();
		int length = videoBytes.length;
        Assert.assertEquals(1024, length);
        videoBytes = new VideoDataProvider().getVideo();
		length = videoBytes.length;
        Assert.assertEquals(1024, length);
        videoBytes = new VideoDataProvider().getVideo();
		length = videoBytes.length;
        Assert.assertEquals(1024, length);
        videoBytes = new VideoDataProvider().getVideo();
		length = videoBytes.length;
        Assert.assertEquals(1024, length);
        videoBytes = new VideoDataProvider().getVideo();
		length = videoBytes.length;
        Assert.assertEquals(1024, length);
        videoBytes = new VideoDataProvider().getVideo();
		length = videoBytes.length;
        Assert.assertEquals(1024, length);
        videoBytes = new VideoDataProvider().getVideo();
		length = videoBytes.length;
        Assert.assertEquals(1024, length);
    }

	/*
	public void testSendVideo() {
		byte[] bytesToSend = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		new VideoDataProvider().sendVideo(bytesToSend);
	}
	*/

}
