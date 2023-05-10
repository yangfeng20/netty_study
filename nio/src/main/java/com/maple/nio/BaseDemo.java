package com.maple.nio;

import sun.nio.ch.FileChannelImpl;

import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yangfeng
 * @date : 2023/5/9 17:22
 * desc:
 */

public class BaseDemo {
    public static void main(String[] args) throws Exception{

        IntBuffer buffer = IntBuffer.allocate(5);



        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put(i);
        }

        // 读写转换
        buffer.flip();

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
