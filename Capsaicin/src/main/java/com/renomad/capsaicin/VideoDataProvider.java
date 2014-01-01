package com.renomad.capsaicin;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Byron on 12/29/13.
 */
public class VideoDataProvider {

    public byte[] getVideo() {
		InputStream myInputStream = null;
		byte[] buffer = new byte[1024];
		Socket mySocket = null;

        try {
			mySocket = new Socket("192.168.56.2", 4321);
			if (mySocket.isConnected()) {
				myInputStream = mySocket.getInputStream();
				myInputStream.read(buffer);
			}
        } catch (IOException e) {
            e.printStackTrace();
			return null;
        } finally {
			closeSocket(mySocket);
		}
		return buffer;
    }

	public void closeSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
