package com.renomad.capsaicin;

import android.provider.MediaStore;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.DatagramChannel;

/**
 * Created by Byron on 12/29/13.
 */
public class VideoDataProvider {

    public byte[] getVideo() {
        DatagramPacket sendPacket = new DatagramPacket(new byte[1024], 1024);
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

        try {
            sendPacket.setData("We are sending this from Android".getBytes());
            sendPacket.setAddress(Inet4Address.getByName("192.168.56.2"));
            sendPacket.setPort(4321);

            DatagramSocket mySocket = new DatagramSocket();

            mySocket.send(sendPacket);
            mySocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivePacket.getData();
    }

    public byte[] sendVideo() {
        DatagramPacket sendPacket = new DatagramPacket(new byte[1024], 1024);
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

        try {
            sendPacket.setData("We are sending this from Android".getBytes());
            sendPacket.setAddress(Inet4Address.getByName("192.168.56.2"));
            sendPacket.setPort(4321);

            DatagramSocket mySocket = new DatagramSocket();

            mySocket.send(sendPacket);
            mySocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivePacket.getData();
    }

}
