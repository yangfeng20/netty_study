package com.maple.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author yangfeng
 * @date : 2023/5/10 11:39
 * desc:
 */

public class SocketChannelDemo {

    public static void main(String[] args) throws Exception {

        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 注册serverSocketChannel，并监听accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("一秒内无事件发生");
                continue;
            }

            // 有事件发生的key集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                // 服务端accept事件
                if (selectionKey.isAcceptable()) {

                    System.out.println("客户端连接成功:" + selectionKey.hashCode());

                    // 给客户端分配一个channel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 并注册到selector中
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(100));
                }

                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();

                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    int readLength;
                    System.out.println("客户端发送的数据为：");
                    do {
                        // 写模式
                        buffer.clear();
                        readLength = channel.read(buffer);

                        // 读模式
                        //buffer.flip();
                        String data = new String(buffer.array(), 0, readLength);
                        System.out.print(data);
                        if (data.endsWith("\r\n")) {
                            break;
                        }
                    } while (readLength != -1);

                    System.out.println("------------------------------");
                    ByteBuffer writeBuffer = ByteBuffer.allocate(10);
                    writeBuffer.put("ok".getBytes(StandardCharsets.UTF_8));
                    channel.write(writeBuffer);
                }

                iterator.remove();

            }
        }

    }
}
