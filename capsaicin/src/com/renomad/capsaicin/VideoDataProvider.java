package com.renomad.capsaicin;

import java.util.UUID;
import java.lang.IllegalArgumentException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;
import android.content.Context;
import java.io.File;
import com.renomad.capsaicin.CtlMsg;
import com.renomad.capsaicin.CtlAct;
import com.renomad.capsaicin.Logger;
/**
 * Responsible for low-level handling of data.
 * @author Byron Katz
 */
public class VideoDataProvider {

    private static final String SERVER_IP_ADDR = "192.168.56.2";
    private static final int SERVER_PORT_NUMBER = 4321;


    /**
     * Requests video from server.
     * @param id id of the video we want
     * @return a valid InputStream, or null
     */
    public InputStream getVidInStrm(CtlMsg cm, String ip_addr, int port) {
	Logger.log("cm: " + cm.toString());
	InputStream myInputStream = null;
	OutputStream myOutputStream = null;
	Socket mySocket = null;
	Logger.log("about to open socket");

	try {
	    mySocket = new Socket();
	    InetSocketAddress addr = new InetSocketAddress(ip_addr, port);
	    int timeout = 1 * 1000; // milliseconds
	    mySocket.connect(addr, timeout);
	    if (mySocket != null && mySocket.isConnected()) {
		Logger.log("socket opened and connected");

		//get the streams from socket.
		myInputStream = mySocket.getInputStream();
		myOutputStream = mySocket.getOutputStream();

		Logger.log("about to tell socket we want a video");
		myOutputStream.write(cm.getMessage()); //we want a video
		Logger.log("successfully requested a video");
	    }
	} catch (UnknownHostException e) {
	    //TODO - BK - 1/16/2014 - need to show a message that
	    //system could not resolve hostname.  Perhaps they are
	    //blocking dns ports or their dns is broken.
	    //ALSO - if this exception is hit, then the socket did not
	    //get opened, so we don't need to close it.
	} catch (IOException e) {
	    //TODO - BK - 1/16/2014 - need to figure out which exceptions
	    //need to be handled with closing the socket and which don't.
	    //TODO - based on IOException, push varying values to toast.
	    if (mySocket != null && mySocket.isConnected()) {
		try {
		    mySocket.close();
		} catch (IOException i) {
		    //TODO - BK 1/18/2014 - if closing the socket fails,
		    //what does that mean?  how to handle?
		}
	    }
	}
	return myInputStream;
    }

    /**
     * Conductor of information flow.
     * Separating out allows us to test these
     * functions independently.  This portion is pure integration.
     * @param id id of video
     * @param bytecount count of bytes we want to transfer from server.
     * @return number of bytes received
     */
    public int grabSomeVideoData(byte[] buf, CtlMsg cm, int bytecount) {
	int result = 0;
	InputStream istrm = 
	    getVidInStrm(cm, SERVER_IP_ADDR, SERVER_PORT_NUMBER);
	if (istrm != null) {
	    result = getVidFromStrm(buf, istrm, bytecount);
	    Logger.log("null inputstream in grabSomeVideoData");
	}
	return result;
    }

    /**
     * Controller to transfer a video into the client.
     *
     * @param context Context from the activity.
     * @param id Id of video we want
     */
    public void controlVideoInTrans(Context context, byte id) {
	final int buffer_size = 1024;
	File file = context.getCacheDir();
	byte[] buffer = new byte[buffer_size];
	int bytesReceived; //TODO - BK - 1/18/2014 - do what with this?

	//TODO - BK - 1/23/2014 - need to use something other than
	//hard-coded values below.
	int sid = 1; //fake session, 1
	UUID vid = new UUID(0,5); //fake video, 5
	UUID uid = new UUID(0,1); //fake user, 1
	long offset = 0; //beginning of file
	short sbytes = 1024; // 1024 bytes of data
	CtlMsg cm = new CtlMsg(sid, CtlAct.CLIENT_WANTS_VID, vid, uid, 
			       offset, sbytes);
	bytesReceived = grabSomeVideoData(buffer, cm, buffer_size);
	writeToFile(buffer, file);
    }

    /**
     * Writes a data buffer to a file.
     * @param data data to save
     * @param file place to save the data.
     */
    public void writeToFile(byte[] data, File file) {
	try {
	    FileOutputStream fis = new FileOutputStream(file);
	    fis.write(data, 0, data.length);
	    fis.flush(); 
	    //TODO - BK - 1/18/2014 - necessary at this point? check.
	} catch (FileNotFoundException e) {
	    //TODO - BK - 1/18/2014 - if file not found, what to do?
	} catch (IOException e) {
	    //TODO - BK - 1/18/2014 - handle writing incorrectly, whatever.
	}

    }

    /**
     * Given a valid InputStream, this method
     * will pull as many bytes as requested.
     * @param buf Buffer in which to store data.
     * @param istrm InputStream to pull data from.
     * @param bytecount Number of bytes to pull.
     * @return number of bytes received
     */
    public int getVidFromStrm(
			      byte[] buf, InputStream istrm, int bytecount) {
	Logger.log("entering getVidFromStrm, buf.length is "+buf.length+
		   ", bytecount is "+bytecount+", and istrm "+
		   ((istrm == null) ? "is" : "is not")+" null");
	if (bytecount > buf.length) {
	    String msg = "bytecount cannot be more than buf size";
	    throw new IllegalArgumentException(msg);
	}
	int result = 0;
	try {
	    Logger.log("about to read from inputstream");
	    result = istrm.read(buf, 0, bytecount);
	    Logger.log("read from socket");
	} catch (IOException e) {
	    //TODO - BK 1/17/2014 - need to handle situation where
	    //reading the buffer does not happen like we think it ought.
	    //perhaps preparing a message for the toast?
	}
	return result;
    }

    /**
     * Open a socket for sending server a video.
     * @param id Id of video to send
     */
    //	public InputStream getVidOutStrm(byte id) {
    //		//we open a socket, tell server what we want to send.
    //		//TODO - BK 1/15/2014 - finish
    //		
    //	}

    /**
     * TODO - BK - ON HOLD until later.  First get getVideo going
     *
     */
    //	public void sendVideo(byte[] toSend, byte id) {
    //		InputStream myInputStream = null;
    //		OutputStream myOutputStream = null;
    //		byte[] receive_buffer = new byte[1024];
    //		CtlMsg sm = new CtlMsg(
    //				(byte)ACTION_CLIENT_SENDING_SERVER_VIDEO, (byte)id);
    //		byte[] request_buffer = {ACTION_CLIENT_SENDING_SERVER_VIDEO, id};
    //		Socket mySocket = null;
    //
    //        try {
    //			mySocket = new Socket("192.168.56.2", SERVER_PORT_NUMBER);
    //			Logger.log("sendVideo", 
    //					"created new socket at " + SERVER_PORT_NUMBER);
    //			if (mySocket.isConnected()) {
    //				Logger.log("sendVideo", "socket is connected");
    //				myInputStream = mySocket.getInputStream();
    //				myOutputStream = mySocket.getOutputStream();
    //				
    //				myOutputStream.write(sm.getMessage()); //"..we want to send"
    //				Logger.log("sendVideo", 
    //					"requesting to send server a video " + id.toString());
    //
    //				myInputStream.read(receive_buffer); //"OK.."
    //				Logger.log("sendVideo", 
    //						"received back " + receive_buffer.toString());
    //
    //				myOutputStream.write(toSend); //"..sending bytes"
    //				Logger.log("sendVideo", "");
    //			}
    //		} catch (Exception e) {
    //			throw e; //controller should handle this
    //        } finally {
    //			closeSocket(mySocket);
    //		}
    //	}
    //
    //	public void closeSocket(Socket socket) {
    //		try {
    //			socket.close();
    //		} catch (Exception e) {
    //			throw e; //controller should handle this
    //			//If we cannot handle closing a socket, what
    //			//does that mean?  Investigate
    //			//TODO - determine what this would mean.
    //		}
    //	}

}
