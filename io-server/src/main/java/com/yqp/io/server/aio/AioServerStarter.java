package com.yqp.io.server.aio;

import com.yqp.io.common.Constant;

import java.io.IOException;

public class AioServerStarter {

    public static void main(String[] args) {

        try {
            Thread t = new Thread(new AioServerRunnable(Constant.SERVER_PORT));
            t.start();
        } catch (IOException io) {
            System.err.println("error in AioServer main start");
            io.printStackTrace();
        }
    }

}
