package com.maple.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yangfeng
 * @date : 2023/8/14 14:45
 * desc:
 */

public class NioFileCopyDemo1 {
    public static void main(String[] args) throws Exception {

        String path = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(), "utf-8");
        FileInputStream inputStream = new FileInputStream(path + "copy-source.txt");
        FileOutputStream outputStream = new FileOutputStream(path + "copy-target.txt");

        FileChannel channel = inputStream.getChannel();
        FileChannel outChannel = outputStream.getChannel();

        channel.transferTo(0, channel.size(), outChannel);

        ByteBuffer buffer = ByteBuffer.allocate(2);

        //while (true){
        //    int readLen = channel.read(buffer);
        //    if (readLen==-1){
        //        break;
        //    }
        //
        //    // buffer切换为读模式，往channel中写数据
        //    buffer.flip();
        //    outChannel.write(buffer, readLen);
        //    buffer.flip();
        //}

        System.out.println("结束");

    }
}
