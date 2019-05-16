package com.yqp.io.client.aio;

import com.yqp.io.common.Constant;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class ClientConnectCompletionHandler implements CompletionHandler<Void, Void> {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    private String name;

    public ClientConnectCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel, String name) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.name = name;
    }

    @Override
    public void completed(Void v1, Void v2) {
        write("hello I'm " + name);
    }

    @Override
    public void failed(Throwable exc, Void v) {
        System.err.println("error in ClientConnectCompletionHandler failed");
        exc.printStackTrace();
    }

    private void write(String data) {
        byte[] req = data.getBytes(Charset.forName(Constant.DEFAULT_CHARSET));
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        asynchronousSocketChannel.write(writeBuffer, null, new ClientWriteCompletionHandler(asynchronousSocketChannel, writeBuffer));
    }
}
