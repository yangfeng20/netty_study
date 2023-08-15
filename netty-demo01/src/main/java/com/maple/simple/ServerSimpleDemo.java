package com.maple.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.channels.Selector;

/**
 * @author yangfeng
 * @date : 2023/8/15 14:28
 * desc:
 */

public class ServerSimpleDemo {
    public static void main(String[] args) {
        new ServerBootstrap()
                // 创建一个事件循环组，相当于是一个selector
                .group(new NioEventLoopGroup())
                // 注册服务端channel实现
                .channel(NioServerSocketChannel.class)
                // 添加读写事件的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println("收到客户端发送的数据：" + msg);
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
