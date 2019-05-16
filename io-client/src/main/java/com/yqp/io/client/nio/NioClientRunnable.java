package com.yqp.io.client.nio;

import com.yqp.io.common.Constant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NioClientRunnable implements Runnable {

    private String name;

    private Socket socket;

    private InputStream in;

    private OutputStream out;

    public NioClientRunnable(String name, Socket socket) throws IOException {
        this.name = name;
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            writeData("hello I'm " + name);
            socket.shutdownOutput();
            readData();
        } catch (IOException io) {
            System.err.println("error in NioClientRunnable");
            io.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException fio) {
                System.err.println("error in NioClientRunnable close socket");
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
        System.out.println(name + " get message from server: " + byteArray.toString(Constant.DEFAULT_CHARSET));
    }

    private void writeData(String data) throws IOException {
        out.write(data.getBytes(Constant.DEFAULT_CHARSET));
    }

}
