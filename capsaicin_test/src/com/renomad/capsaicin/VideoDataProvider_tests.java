package com.renomad.capsaicin.tests;

import com.renomad.capsaicin.VideoDataProvider;
import com.renomad.capsaicin.CtlMsg;
import com.renomad.capsaicin.CtlAct;
import java.io.InputStream;
import junit.framework.*;
import java.util.Arrays;

/**
 * These tests cover sending and receiving raw data for videos.  
 * They require a server to communicate.
 * @author Byron Katz
 */
public class VideoDataProvider_tests extends TestCase{

    public void testGettingSomeData3() {
			VideoDataProvider vdp = new VideoDataProvider();
			byte id = 5;
			CtlMsg cm = new CtlMsg(CtlAct.CLIENT_WANTS_VID, id);
			String ip_addr = "192.168.56.2";
			int port = 4321;
			InputStream iStrm = vdp.getVidInStrm(cm, ip_addr, port);
			Assert.assertTrue(iStrm != null);
			byte[] buf = new byte[2067753];
			int result = vdp.getVidFromStrm(buf, iStrm, 0);
			Assert.assertTrue(buf.length > 0);
			System.out.println("test3: buf length is " + buf.length);
			System.out.println("test3: result is " + result);
    }

    public void testGettingSomeData2() {
			VideoDataProvider vdp = new VideoDataProvider();
			byte id = 5;
			CtlMsg cm = new CtlMsg(CtlAct.CLIENT_WANTS_VID, id);
			String ip_addr = "192.168.56.2";
			int port = 4321;
			InputStream iStrm = vdp.getVidInStrm(cm, ip_addr, port);
			Assert.assertTrue(iStrm != null);
			byte[] buf = new byte[2067753];
			int result = vdp.getVidFromStrm(buf, iStrm, 2067753);
			Assert.assertTrue(buf.length > 0);
			System.out.println("test2: buf length is " + buf.length);
			System.out.println("test2: result is " + result);
    }

    public void testGettingSomeData1() {
			VideoDataProvider vdp = new VideoDataProvider();
			byte id = 5;
			CtlMsg cm = new CtlMsg(CtlAct.CLIENT_WANTS_VID, id);
			String ip_addr = "192.168.56.2";
			int port = 4321;
			InputStream iStrm = vdp.getVidInStrm(cm, ip_addr, port);
			Assert.assertTrue(iStrm != null);
			byte[] buf = new byte[2067753];
			int result = vdp.getVidFromStrm(buf, iStrm, 2067753-1000);
			Assert.assertTrue(result > 0);
			System.out.println("test1: buf length is " + buf.length);
			System.out.println("test1: result is " + result);
    }

    public void testGettingInputStreamOnGetVideo() {
			VideoDataProvider vdp = new VideoDataProvider();
			byte id = 5;
			CtlMsg cm = new CtlMsg(CtlAct.CLIENT_WANTS_VID, id);
			String ip_addr = "192.168.56.2";
			int port = 4321;
			InputStream iStrm = vdp.getVidInStrm(cm, ip_addr, port);
			Assert.assertTrue(iStrm != null);
    }

//	public void testSendVideo() {
//		byte[] bytesToSend = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//		byte id = 6;
//		VideoDataProvider vdp = new VideoDataProvider();
//		vdp.sendVideo(bytesToSend, id);
//	}

}
