package edu.zhku.jsj144.lzc.video.util.uploadUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 上传组件
 */
public class UploadClient {

    private static String uid = null;

    public static void setUid(String uid) {
        UploadClient.uid = uid;
    }

    public static void startUpload(final String localFilepath, final String vid) throws InterruptedException {
        if (uid == null) {
            throw new IllegalStateException("uid is null");
        }
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) {
					ch.pipeline().addLast("decoder",
							new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
					ch.pipeline().addLast("encoder", new ObjectEncoder());
					ch.pipeline().addLast("handler", new FileInfoClientHandler(uid, localFilepath, vid));
				}

			});

			ChannelFuture channelFuture = bootstrap.connect("localhost", 8086).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}
