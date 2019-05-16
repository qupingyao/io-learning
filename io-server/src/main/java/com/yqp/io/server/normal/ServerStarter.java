package com.yqp.io.server.normal;

import com.yqp.io.common.Constant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStarter {

    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(Constant.SERVER_PORT);
            int i = 1;
            while (true) {
                Socket socket = server.accept();
                Thread t = new Thread(new ServerRunnable("server-" + i, socket));
                t.start();
                i++;
            }
        } catch (IOException io) {
            System.err.println("error in Server main start");
            io.printStackTrace();
        }
    }
}
