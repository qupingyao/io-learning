package com.yqp.io.client.aio;

import com.yqp.io.common.Constant;
import java.net.InetSocketAddress;

public class AioClientStarter {

    public static void main(String[] args) {
        try {
            InetSocketAddress remote = new InetSocketAddress(Constant.SERVER_IP, Constant.SERVER_PORT);
            for (int i = 1; i <= 9; i++) {
                InetSocketAddress local = new InetSocketAddress(Constant.CLIENT_IP, Constant.BASE_CLIENT_PORT + i);
                Thread t = new Thread(new AioClientRunnable("client-" + i, local, remote));
                t.start();
            }
        } catch (Exception io) {
            System.err.println("error in AioClient main start");
            io.printStackTrace();
        }
    }

}
