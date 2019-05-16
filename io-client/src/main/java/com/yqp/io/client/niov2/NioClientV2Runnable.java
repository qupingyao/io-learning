package com.yqp.io.client.niov2;

import com.yqp.io.common.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NioClientV2Runnable implements Runnable {

    private String name;

    private SelectorProvider provider = SelectorProvider.provider();

    private Selector selector = provider.openSelector();

    private SocketChannel socketChannel = provider.openSocketChannel();

    public NioClientV2Runnable(String name, InetSocketAddress local, InetSocketAddress remote) throws IOException {
        this.name = name;
        socketChannel.configureBlocking(false);
        socketChannel.bind(local);
        socketChannel.connect(remote);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
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
                    if (key.isConnectable()) {
                        socketChannel.finishConnect();
                        key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
                        key.interestOps(key.interestOps() | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    } else {
                        SocketChannel channel = (SocketChannel) key.channel();
                        if (key.isValid() && key.isWritable()) {
                            write(channel, "hello I'm " + name);
                            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                        }
                        if (key.isValid() && key.isReadable()) {
                            int len = read(channel);
                            if (len < 0) {
                                channel.close();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("error in NioServerV2Runnable");
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
                selector.close();
            } catch (IOException fio) {
                System.err.println("error in NioServerV2Runnable close channel/selector");
                fio.printStackTrace();
            }

        }
    }

    private static int read(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        StringBuffer sBuf = new StringBuffer();
        int len;
        while ((len = channel.read(buffer)) > 0) {
            sBuf.append(new String(buffer.array(), 0, len, Charset.forName(Constant.DEFAULT_CHARSET)));
            buffer.clear();
        }
        if (sBuf.toString().length() > 0) {
            System.out.println("get message from server: " + sBuf.toString());
        }
        return len;
    }

    private static void write(SocketChannel channel, String msg) throws IOException {
        byte[] bytes = msg.getBytes(Constant.DEFAULT_CHARSET);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        channel.write(buffer);
    }

}
