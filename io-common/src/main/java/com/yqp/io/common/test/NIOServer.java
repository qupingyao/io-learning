package com.yqp.io.common.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
    private static ByteBuffer bf = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    System.out.println("连接准备就绪");
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    System.out.println("读准备就绪");
                    SocketChannel channel = (SocketChannel) key.channel();
                    int readLen = 0;
                    bf.clear();
                    StringBuffer buffer = new StringBuffer();
                    while ((readLen = channel.read(bf)) > 0) {
                        buffer.append(new String(bf.array()));
                        bf.clear();
                    }
                    if (-1 == readLen) {
                        channel.close();
                    }
                    String str = "客户端，你传过来的数据是：" + buffer.toString();
                    System.out.println("str:"+str);
                    channel.write(ByteBuffer.wrap(str.getBytes()));
                }
                it.remove();
            }
        }
    }

}
