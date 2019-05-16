package com.yqp.io.client.niov2;

import com.yqp.io.common.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NioClientV2Starter {

    public static void main(String[] args) {

        try {
            InetSocketAddress remote = new InetSocketAddress(Constant.SERVER_IP, Constant.SERVER_PORT);
            for (int i = 1; i <= 9; i++) {
                InetSocketAddress local = new InetSocketAddress(Constant.CLIENT_IP, Constant.BASE_CLIENT_PORT + i);
                Thread t = new Thread(new NioClientV2Runnable("client-" + i, local, remote));
                t.start();
            }
        } catch (IOException io) {
            System.err.println("error in NioClientV2 main start");
            io.printStackTrace();
        }
    }
}
