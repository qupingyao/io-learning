package com.yqp.io.client.aio;

import com.yqp.io.common.util.ThreadUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

public class AioClientRunnable implements Runnable{

    private AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open();

    private String name;

    public AioClientRunnable(String name, InetSocketAddress local, InetSocketAddress remote) throws IOException,InterruptedException {
        this.name = name;
        asynchronousSocketChannel.bind(local);
        asynchronousSocketChannel.connect(remote,null,new ClientConnectCompletionHandler(asynchronousSocketChannel,name));

    }

    @Override
    public void run(){
        ThreadUtil.hangCurrentThread();
    }

}
