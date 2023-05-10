package com.maple.nio;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yangfeng
 * @date : 2023/5/9 18:16
 * desc:
 */

public class NioDemoRead {
    public static void main(String[] args) throws Exception{

        String path = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(), "utf-8");
        FileInputStream inputStream = new FileInputStream(path + "test.txt");

        FileChannel channel = inputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(100);

        // 读通道中的数据到缓冲区【此时对于buffer是写操作】
        channel.read(buffer);
        buffer.flip();

        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }

        channel.close();
    }
}
