package com.renomad.capsaicin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;

/**
 * Created by Byron on 12/29/13.
 */
public class VideoDataProvider {

	private static final String ACTIVITY_TAG = "VideoDataProvider";

    public byte[] getVideo() {
		InputStream myInputStream = null;
		OutputStream myOutputStream = null;
		byte[] receive_buffer = new byte[1024];
		byte[] request_buffer = {1, 5}; // 1 is request 5 is id
		Socket mySocket = null;

        try {
			mySocket = new Socket("192.168.56.2", 4321);
			if (mySocket.isConnected()) {
				myInputStream = mySocket.getInputStream();
				myOutputStream = mySocket.getOutputStream();
				myOutputStream.write(request_buffer);
				myInputStream.read(receive_buffer);
			}
        } catch (IOException e) {
            e.printStackTrace();
			return null;
        } finally {
			closeSocket(mySocket);
		}
		return receive_buffer;
    }

	public void sendVideo(byte[] toSend) {
		InputStream myInputStream = null;
		OutputStream myOutputStream = null;
		byte[] receive_buffer = new byte[1024];
		byte[] request_buffer = {2}; // 2 means we are sending to server
		Socket mySocket = null;

        try {
			mySocket = new Socket("192.168.56.2", 4321);
			if (mySocket.isConnected()) {
				myInputStream = mySocket.getInputStream();
				myOutputStream = mySocket.getOutputStream();
				
				myOutputStream.write(request_buffer); //"..we want to send"
				myInputStream.read(receive_buffer); //"OK.."
				myOutputStream.write(toSend); //"..sending bytes"
			}
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
			closeSocket(mySocket);
		}
	}

	public void closeSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
