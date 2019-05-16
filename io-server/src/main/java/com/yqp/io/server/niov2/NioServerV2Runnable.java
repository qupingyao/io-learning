package com.yqp.io.server.niov2;

import com.yqp.io.common.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NioServerV2Runnable implements Runnable {

    private SelectorProvider provider = SelectorProvider.provider();

    private Selector selector = provider.openSelector();

    private ServerSocketChannel serverSocketChannel = provider.openServerSocketChannel();

    public NioServerV2Runnable(int port) throws IOException {
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else {
                        SocketChannel channel = (SocketChannel) key.channel();
                        if (key.isValid() && key.isWritable()) {
                            write(channel, "hello I'm server");
                            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                            key.channel().close();
                        }
                        if (key.isValid() && key.isReadable()) {
                            read(channel);
                            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("error in NioServerV2Runnable");
            e.printStackTrace();
        } finally {
            try {
                serverSocketChannel.close();
                selector.close();
            } catch (IOException fio) {
                System.err.println("error in NioServerV2Runnable close channel/selector");
                fio.printStackTrace();
            }

        }
    }

    private static void read(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        StringBuffer sBuf = new StringBuffer();
        int len;
        while ((len = channel.read(buffer)) > 0) {
            sBuf.append(new String(buffer.array(), 0, len, Charset.forName(Constant.DEFAULT_CHARSET)));
            buffer.clear();
        }
        if (sBuf.toString().length() > 0) {
            System.out.println("get message from client: " + sBuf.toString());
        }
    }

    private static void write(SocketChannel channel, String msg) throws IOException {
        byte[] bytes = msg.getBytes(Constant.DEFAULT_CHARSET);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.clear();
        channel.write(buffer);
    }

}
