package com.maple.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;

/**
 * @author yangfeng
 * @date : 2023/5/10 9:46
 * desc:
 */

public class NioTranDemo {
    public static void main(String[] args) throws Exception {
        String path = URLDecoder.decode(ClassLoader.getSystemResource("").getPath(), "utf-8");

        FileInputStream inputStream = new FileInputStream(path + "copy.txt");
        FileChannel inputChannel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream(path + "copy01.txt");
        FileChannel outputChannel = outputStream.getChannel();

        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

        inputStream.close();
        outputStream.close();
    }
}
