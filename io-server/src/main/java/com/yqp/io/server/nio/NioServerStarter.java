package com.yqp.io.server.nio;

import com.yqp.io.common.Constant;

import java.io.IOException;

public class NioServerStarter {

    public static void main(String[] args) {

        try {
            Thread t = new Thread(new NioServerRunnable(Constant.SERVER_PORT));
            t.start();
        } catch (IOException io) {
            System.err.println("error in NioServer main start");
            io.printStackTrace();
        }
    }
}
