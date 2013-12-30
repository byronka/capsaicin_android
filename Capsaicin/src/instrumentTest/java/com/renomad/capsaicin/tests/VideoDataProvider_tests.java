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
        byte[] little_byte = new VideoDataProvider().getVideo();
        byte[] expected_byte = new byte[] {1,2,3,4};
        Assert.assertTrue(String.format("was actually %s", new String(little_byte)), Arrays.equals(expected_byte, little_byte));
    }

    public void testSendVideo() {
        byte[] little_byte = new VideoDataProvider().sendVideo();
        byte[] expected_byte = new byte[] {1,2,3,4};
        Assert.assertTrue(String.format("was actually %s", new String(little_byte)), Arrays.equals(expected_byte, little_byte));
    }
}
