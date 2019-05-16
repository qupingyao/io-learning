package com.yqp.io.server.aio;

import com.yqp.io.common.util.ThreadUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class AioServerRunnable implements Runnable{

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();

    public AioServerRunnable(int port) throws IOException {
        asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
    }

    @Override
    public void run(){
        asynchronousServerSocketChannel.accept(null,new ServerAcceptCompletionHandler(asynchronousServerSocketChannel));
        ThreadUtil.hangCurrentThread();
    }

}
