package com.yqp.io.common.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {

    private static Selector selector;

    public static void main(String[]args) throws Exception{
        selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1",8080));
        channel.register(selector,SelectionKey.OP_READ);

        ByteBuffer bf = ByteBuffer.allocate(1024);
        bf.put("Hi,server,i'm client".getBytes());

        channel.write(bf);
        while(true){
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey key = it.next();
                if(key.isReadable()){
                    bf.clear();
                    SocketChannel socketChannel  = (SocketChannel)key.channel();
                    socketChannel.read(bf);
                    System.out.println("服务端返回的数据："+new String(bf.array()));
                }
            }
            selector.selectedKeys().clear();
        }
    }

}
