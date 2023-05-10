package com.maple.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author yangfeng
 * @date : 2023/5/9 20:45
 * desc:
 */

public class NioPrintCurrentJava {
    public static void main(String[] args) throws Exception {
        test01();
    }

    public static void test01() throws Exception {
        Class<?> currentJavaClass = NioPrintCurrentJava.class;

        String classPath = URLDecoder.decode(currentJavaClass.getResource("/").getPath(), "utf-8");

        // 切割路径后面的几个目录【丢弃多少个目录，就length-number】
        String[] segments = classPath.split("/");
        String projectPath = Arrays.stream(segments).limit(segments.length - 3).reduce((a, b) -> a + "/" + b).orElse("");
        System.out.println(projectPath);

        String curClassPath = currentJavaClass.getPackage().getName().replaceAll("\\.", "/");
        String path = projectPath + "/nio/src/main/java/" + curClassPath + "/" + currentJavaClass.getSimpleName() + ".java";

        System.out.println("java文件地址：" + path);

        FileInputStream inputStream = new FileInputStream(path);
        int dataCount = inputStream.available();
        FileChannel channel = inputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(dataCount);
        channel.read(buffer);


        FileOutputStream outputStream = new FileOutputStream(classPath + "copy.txt");
        FileChannel outputChannel = outputStream.getChannel();
        buffer.flip();
        outputChannel.write(buffer);

        FileInputStream fileInputStream = new FileInputStream(classPath + "copy.txt");
        FileChannel inputChannel = fileInputStream.getChannel();

        // buffer对于数据量大小未知的情况下循环读取
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(100);

        byte[] bytes = new byte[100];
        int readCount = 0;
        while ((readCount = inputChannel.read(readBuffer)) !=-1){
            // buffer转换为读模式，用于下面get获取数据
            readBuffer.flip();
            readBuffer.get(bytes, 0, readCount);
            String readData = new String(bytes, 0, readCount);
            System.out.print(readData);

            // 重新切换为写模式，用于后面【inputChannel.read() 往 buffer中写数据】
            readBuffer.flip();
            // 这种方式也可以，作用都是将position=0，其他的可能有细微差别
            readBuffer.clear();
        }

    }
}
