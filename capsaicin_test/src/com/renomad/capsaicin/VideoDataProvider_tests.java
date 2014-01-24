package com.renomad.capsaicin.tests;

import com.renomad.capsaicin.VideoDataProvider;
import com.renomad.capsaicin.CtlMsg;
import com.renomad.capsaicin.CtlAct;
import com.renomad.capsaicin.Logger;
import java.io.InputStream;
import junit.framework.*;
import java.util.UUID;
import java.util.Arrays;

/**
 * These tests cover sending and receiving raw data for videos.  
 * They require a server to communicate.
 * @author Byron Katz
 */
public class VideoDataProvider_tests extends TestCase{


    private void testGettingSomeData_aux(int testid, int bytecount) {
	VideoDataProvider vdp = new VideoDataProvider();
	Logger.log("test"+testid+
		   ": want "+bytecount+" bytes from the server");
	int sid = 1234;
	CtlAct action = CtlAct.CLIENT_WANTS_VID;
	UUID vid = new UUID(0,5);
	UUID uid = new UUID(0,1);
	long offset = 0;
	short sbytes = 1024;
	CtlMsg cm = new CtlMsg(sid, action, vid, uid, offset, sbytes);
	String ip_addr = "192.168.56.2";
	int port = 4321;
	InputStream iStrm = vdp.getVidInStrm(cm, ip_addr, port);
	Assert.assertTrue(iStrm != null);
	byte[] buf = new byte[10];
	int result = vdp.getVidFromStrm(buf, iStrm, bytecount);
	try {
	    iStrm.close();
	} catch (Exception e) {
	    Logger.log("test"+testid+" exception at testGettingSomeData");
	}
	Logger.log("test"+testid+": Actual result: " + result);
	Assert.assertEquals(bytecount, result);
    }

    public void testGettingSomeData1() {
	testGettingSomeData_aux(1, 0);
    }

    public void testGettingSomeData2() {
	testGettingSomeData_aux(2, 1024);
    }

    public void testGettingSomeData3() {
	testGettingSomeData_aux(3, 1023);
    }

    public void testGettingSomeData4() {
	try {
	    testGettingSomeData_aux(4, 1025);
	    Assert.fail("we should have hit an exception for invalid params");
	} catch (IllegalArgumentException e) {
	    //if no failure, we caught the exception 
	}
    }

    public void testGettingInputStreamOnGetVideo() {
	VideoDataProvider vdp = new VideoDataProvider();
	int sid = 1234;
	CtlAct action = CtlAct.CLIENT_WANTS_VID;
	UUID vid = new UUID(0,5);
	UUID uid = new UUID(0,1);
	long offset = 0;
	short sbytes = 1024;
	CtlMsg cm = new CtlMsg(sid, action, vid, uid, offset, sbytes);
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
