package com.yqp.io.server.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerWriteCompletionHandler implements CompletionHandler<Integer,Void> {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    private ByteBuffer writeBuffer;

    public ServerWriteCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel, ByteBuffer byteBuffer){
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.writeBuffer = byteBuffer;
    }

    @Override
    public void completed(Integer result, Void v){
        if(writeBuffer.hasRemaining()){
            asynchronousSocketChannel.write(writeBuffer,null,new ServerWriteCompletionHandler(asynchronousSocketChannel,writeBuffer));
        }
    }

    @Override
    public void failed(Throwable exc, Void v){
        System.err.println("error in ServerWriteCompletionHandler failed");
        exc.printStackTrace();
    }


}
