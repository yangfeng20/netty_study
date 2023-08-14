package com.maple.nio.groupchat;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author yangfeng
 * @date : 2023/5/11 13:52
 * desc:
 */

public class ChatServer {
    public static void main(String[] args) throws Exception {

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));
        server.configureBlocking(false);

        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        server.register(selector, SelectionKey.OP_READ);
        server.register(selector, SelectionKey.OP_CONNECT);


        while (true) {

            // 有io事件发生
            if (selector.select() >= 1) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    handlerClientChannel(selectionKey);
                    iterator.remove();
                }
            }
        }


    }

    private static void handlerClientChannel(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        if (selectionKey.isAcceptable()) {

        }
    }
}
