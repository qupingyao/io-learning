package com.yqp.io.server.aio;

import com.yqp.io.common.Constant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class ServerReadCompletionHandler implements CompletionHandler<Integer,Void> {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    private ByteBuffer readBuffer;

    public ServerReadCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel,ByteBuffer byteBuffer){
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.readBuffer = byteBuffer;
    }

    @Override
    public void completed(Integer integer, Void v){
        read();
        write("hello I'm server");
    }

    @Override
    public void failed(Throwable exc, Void v){
        System.err.println("error in ReadCompletionHandler failed");
        exc.printStackTrace();
    }

    private void read(){
        readBuffer.flip();
        byte[] body = new byte[readBuffer.remaining()];
        readBuffer.get(body);
        String data = new String(body, Charset.forName(Constant.DEFAULT_CHARSET));
        System.out.println("get message from client: " + data);
    }

    private void write(String data){
        byte[] bytes = data.getBytes(Charset.forName(Constant.DEFAULT_CHARSET));
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        asynchronousSocketChannel.write(writeBuffer,null,new ServerWriteCompletionHandler(asynchronousSocketChannel,writeBuffer));
    }

}
