package com.yqp.io.server.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerAcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public ServerAcceptCompletionHandler(AsynchronousServerSocketChannel asynchronousServerSocketChannel){
        this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
    }

    @Override
    public void completed(AsynchronousSocketChannel asynchronousSocketChannel, Void v) {
        asynchronousServerSocketChannel.accept(null, new ServerAcceptCompletionHandler(asynchronousServerSocketChannel));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        asynchronousSocketChannel.read(buffer, null, new ServerReadCompletionHandler(asynchronousSocketChannel,buffer));
    }

    @Override
    public void failed(Throwable t, Void v) {
        System.err.println("error in AcceptCompletionHandler failed");
        t.printStackTrace();
    }
}
