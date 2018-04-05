package edu.zhku.jsj144.lzc.video.util.uploadUtil;

import edu.zhku.jsj144.lzc.video.application.BaseApplication;
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
import io.netty.handler.stream.ChunkedFile;

/**
 * 上传组件
 */
public class UploadClient {

    private static String uid = null;
	protected static ChunkedFile chunkedFile;

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

			ChannelFuture channelFuture = bootstrap.connect(BaseApplication.UPLOAD_BASE_IP, 8086).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

    /**
     * 获取上传进度
     * @return
     */
    public static int getUploadProgress() {
        if (chunkedFile == null) {
            return 0;
        } else {
            return (int) (chunkedFile.currentOffset() * 1.0 / chunkedFile.endOffset() * 100);
        }
    }

}
