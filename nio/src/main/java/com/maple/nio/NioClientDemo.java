package com.maple.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author yangfeng
 * @date : 2023/5/10 15:53
 * desc:
 */

public class NioClientDemo {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);

        if (!socketChannel.connect(inetSocketAddress)) {
            System.out.println("连接服务器中...");

            while (!socketChannel.finishConnect()) {
                //Thread.sleep(500);
                //System.out.println("服务器连接中...其他事件");
            }
        }


        ByteBuffer buffer = ByteBuffer.wrap("客户端数据\r\n".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next() + "\r\n";
            buffer.clear();
            socketChannel.write(ByteBuffer.wrap(next.getBytes(StandardCharsets.UTF_8)));
        }

    }
}
