package com.yqp.io.client.aio;

import com.yqp.io.common.Constant;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class ClientReadCompletionHandler implements CompletionHandler<Integer,Void> {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    private ByteBuffer readBuffer;

    public ClientReadCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel,ByteBuffer byteBuffer){
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.readBuffer = byteBuffer;
    }

    @Override
    public void completed(Integer integer, Void v){
        read();
    }

    @Override
    public void failed(Throwable exc, Void v){
        System.err.println("error in ClientReadCompletionHandler failed");
        exc.printStackTrace();
    }

    private void read(){
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);
        String data = new String(bytes, Charset.forName(Constant.DEFAULT_CHARSET));
        System.out.println("get data from server:" + data);
    }


}

