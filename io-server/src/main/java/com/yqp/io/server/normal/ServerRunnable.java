package com.yqp.io.server.normal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.yqp.io.common.Constant;

public class ServerRunnable implements Runnable {

    private String name;

    private Socket socket;

    private InputStream in;

    private OutputStream out;

    public ServerRunnable(String name, Socket socket) throws IOException {
        this.name = name;
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            readData();
            writeData("hello I'm " + name);
            socket.shutdownOutput();
        } catch (IOException io) {
            System.err.println("error in ServerRunnable");
            io.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException fio) {
                System.err.println("error in ServerRunnable close socket");
                fio.printStackTrace();
            }
        }
    }

    private void readData() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        int content;
        while ((content = in.read()) != -1) {
            byteArray.write(content);
        }
        System.out.println(name + " get message from client: " + byteArray.toString(Constant.DEFAULT_CHARSET));
    }

    private void writeData(String data) throws IOException {
        out.write(data.getBytes(Constant.DEFAULT_CHARSET));
    }
}
