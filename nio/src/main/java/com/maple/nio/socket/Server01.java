package com.maple.nio.socket;

import cn.hutool.core.lang.Console;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author yangfeng
 * @date : 2023/8/14 16:36
 * desc:
 */

public class Server01 {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress(8080));

        Console.print("等待连接\n");
        while (true){
            SocketChannel channel = socketChannel.accept();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);

            // 读数据
            buffer.flip();
            Console.print("读取数据：{}", StandardCharsets.UTF_8.decode(buffer));
            // 返回响应结果
            channel.write(StandardCharsets.UTF_8.encode("服务器结果"));
        }
    }
}
