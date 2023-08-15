package com.maple.nio.selector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author yangfeng
 * @date : 2023/8/15 9:28
 * desc:
 */

public class ServerSelectThreadDemo {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector acceptSelector = Selector.open();
        serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT, null);

        Work work = new Work("work-0");
        while (true) {
            acceptSelector.select();
            Set<SelectionKey> selectionKeys = acceptSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                work.register(socketChannel);
                iterator.remove();
            }
        }
    }


    static class Work implements Runnable {

        private volatile boolean start;
        private Selector readerSelector;
        private String name;


        public Work(String name) throws Exception {
            this.name = name;
            readerSelector = Selector.open();
        }

        public void register(SocketChannel socketChannel) throws Exception {
            if (!start) {
                Thread thread = new Thread(this, name);
                thread.start();
                start = true;
            }
            readerSelector.wakeup();
            socketChannel.register(readerSelector, SelectionKey.OP_READ, null);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    readerSelector.select();
                    Set<SelectionKey> selectionKeys = readerSelector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        SocketChannel channel = (SocketChannel) next.channel();
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
