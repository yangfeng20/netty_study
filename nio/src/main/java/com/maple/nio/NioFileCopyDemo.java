package com.maple.nio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yangfeng
 * @date : 2023/5/10 9:26
 * desc:
 */

public class NioFileCopyDemo {
    public static void main(String[] args) throws Exception {
        String path = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(), "utf-8") + "/copy.txt";


        FileInputStream inputStream = new FileInputStream(path);
        FileChannel channel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("/copy01.txt");
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
        int readLength;
        do {
            readLength = channel.read(buffer);

            // 转换为读模式
            buffer.flip();
            outputChannel.write(buffer);

            // 转换为写模式
            buffer.clear();
        } while (readLength != -1);

        new BufferedInputStream(new FileInputStream("aa")).close();
    }
}
