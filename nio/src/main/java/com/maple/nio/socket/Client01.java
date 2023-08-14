package com.maple.nio.socket;

import cn.hutool.core.lang.Console;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author yangfeng
 * @date : 2023/8/14 16:51
 * desc:
 */

public class Client01 {
    public static void main(String[] args) throws Exception {
        SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 8080));
        channel.write(StandardCharsets.UTF_8.encode("测试请求数据"));


        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        Console.print("响应数据: {}\n", StandardCharsets.UTF_8.decode(buffer));
    }
}
