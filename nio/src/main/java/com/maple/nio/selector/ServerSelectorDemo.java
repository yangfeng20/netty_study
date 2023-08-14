package com.maple.nio.selector;

import cn.hutool.core.lang.Console;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author yangfeng
 * @date : 2023/8/14 17:06
 * desc:
 */

public class ServerSelectorDemo {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.bind(new InetSocketAddress(8080));
        Selector selector = Selector.open();

        // 将channel注册到channel中，并指定监听的事件
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_ACCEPT, null);
        Console.print("accept的key:{}\n", selectionKey);


        while (true) {
            // 没有事件就阻塞,或者是有事件未处理
            selector.select();

            // 所有当前发生的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey curKey = iterator.next();
                eventHandler(curKey, selector);
                // 需要删除【不会自动移除，不然这个事件还存在，但是由于由于事件channel已经处理，channel为空】
                iterator.remove();
            }

        }
    }


    private static void eventHandler(SelectionKey selectionKey, Selector selector) throws Exception {
        // 客户端连接事件
        if (selectionKey.isAcceptable()) {
            // 获取注册进selector中的原始channel
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = channel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ, null);
        } else if (selectionKey.isReadable()) {
            // 读客户端数据
            SocketChannel readChannel = ((SocketChannel) selectionKey.channel());
            // todo 这里会有消息边界，也就是半包问题；只能读取buffer容量的数据，不能读取全部数据，导致有多次读事件；但是数据已经拆分为多个业务
            ByteBuffer buffer = ByteBuffer.allocate(3);
            try {
                //客户端断开链接，会触发read事件；异常断开，read报错；正常断开，readLen=-1
                int readLen = readChannel.read(buffer);
                if (readLen == -1){
                    selectionKey.cancel();
                    return;
                }
            } catch (IOException e) {
                Console.print("客户端关闭: {}\n", readChannel);
                // 移除当前事件
                selectionKey.cancel();
            }
            buffer.flip();
            String msg = StandardCharsets.UTF_8.decode(buffer).toString();
            Console.print("服务端read: {}\n", msg);
        }
    }
}
