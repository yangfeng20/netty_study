package com.maple.bioserve;


import cn.hutool.core.io.IoUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yangfeng
 * @date : 2023/5/9 10:47
 * desc:
 */

public class BioServeDemo {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8090);


        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("客户端连接成功");
            threadPool.execute(() -> {
                handlerClientSocket(socket);
            });
        }
    }

    private static void handlerClientSocket(Socket socket) {

        byte[] readByteArr = new byte[1024];
        int readOff = 0;
        try {

            BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
            readOff = inputStream.read(readByteArr);

            do {
                System.out.println(new String(readByteArr, 0, readOff));

                readOff = inputStream.read(readByteArr);
            } while (readOff != -1);

            OutputStream outputStream = socket.getOutputStream();
            IoUtil.write(outputStream, "utf-8", false, "响应");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
