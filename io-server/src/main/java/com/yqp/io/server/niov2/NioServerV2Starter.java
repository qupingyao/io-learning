package com.yqp.io.server.niov2;

import com.yqp.io.common.Constant;

import java.io.IOException;

public class NioServerV2Starter {

    public static void main(String[] args) {

        try {
            Thread t = new Thread(new NioServerV2Runnable(Constant.SERVER_PORT));
            t.start();
        } catch (IOException io) {
            System.err.println("error in NioServerV2 main start");
            io.printStackTrace();
        }
    }
}
