package com.yqp.io.client.aio;

import com.yqp.io.common.Constant;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ClientWriteCompletionHandler implements CompletionHandler<Integer,Void> {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    private ByteBuffer writeBuffer;

    public ClientWriteCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel,ByteBuffer writeBuffer){
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.writeBuffer = writeBuffer;
    }

    @Override
    public void completed(Integer result, Void v2){
        if(writeBuffer.hasRemaining()){
            asynchronousSocketChannel.write(writeBuffer,null, new ClientWriteCompletionHandler(asynchronousSocketChannel,writeBuffer));
        }else{
            read();
        }
    }

    @Override
    public void failed(Throwable exc, Void v){
        System.err.println("error in ClientWriteCompletionHandler failed");
        exc.printStackTrace();
    }

    private void read(){
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        asynchronousSocketChannel.read(readBuffer, null, new ClientReadCompletionHandler(asynchronousSocketChannel,readBuffer));
    }

}
