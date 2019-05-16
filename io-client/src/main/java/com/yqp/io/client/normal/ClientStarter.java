package com.yqp.io.client.normal;

import com.yqp.io.common.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientStarter {

    public static void main(String[] args) {

        try {
            for (int i = 1; i <= 9; i++) {
                Socket socket = new Socket();
                socket.bind(new InetSocketAddress(Constant.CLIENT_IP, Constant.BASE_CLIENT_PORT + i));
                socket.connect(new InetSocketAddress(Constant.SERVER_IP, Constant.SERVER_PORT));
                Thread t = new Thread(new ClientRunnable("client-" + i, socket));
                t.start();
            }
        } catch (IOException io) {
            System.err.println("error in Client main start");
            io.printStackTrace();
        }
    }
}
