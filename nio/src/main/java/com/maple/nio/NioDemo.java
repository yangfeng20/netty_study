package com.maple.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yangfeng
 * @date : 2023/5/9 18:03
 * desc:
 */

public class NioDemo {
    public static void main(String[] args) throws Exception{
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        path = URLDecoder.decode(path, "utf-8");
        System.out.println(path);


        FileOutputStream outputStream = new FileOutputStream(path + "/test.txt");
        FileChannel channel = outputStream.getChannel();


        ByteBuffer buffer = ByteBuffer.allocate(100);

        // buffer模式是写模式
        buffer.put("hello world".getBytes());


        // 切换读写模式，此时转换为读模式
        buffer.flip();

        // 缓冲区数据写入通道 【此时对于buffer是读操作】
        channel.write(buffer);
        outputStream.close();

    }

}
