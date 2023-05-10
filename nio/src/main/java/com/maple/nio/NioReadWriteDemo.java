package com.maple.nio;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author yangfeng
 * @date : 2023/5/9 20:21
 * desc:
 */

public class NioReadWriteDemo {

    private static String basePath;

    static {
        try {
            basePath = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "result.txt", "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws Exception {
        write();
        read();
    }


    public static void write() throws Exception {
        FileOutputStream outputStream = new FileOutputStream(basePath);
        FileChannel channel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
        buffer.put("hello world 我是数据".getBytes(StandardCharsets.UTF_8));

        buffer.flip();
        channel.write(buffer);
        channel.close();
        //outputStream.flush();
        //outputStream.close();
    }


    public static void read() throws Exception {
        FileInputStream inputStream = new FileInputStream(basePath);
        FileChannel channel = inputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
        channel.read(buffer);

        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);

        System.out.println(new String(bytes));

    }



}
