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
        String returned_bytes_string = new String(new VideoDataProvider().getVideo()).trim();
        String expected_bytes_string = "TESTTESTOK";
        Assert.assertEquals(returned_bytes_string, expected_bytes_string, returned_bytes_string);
    }

    public void testSendVideo() {
        String returned_bytes_string = new String(new VideoDataProvider().sendVideo()).trim();
        String expected_bytes_string = "TESTTESTOK";
        Assert.assertEquals(returned_bytes_string, expected_bytes_string, returned_bytes_string);
    }
}
