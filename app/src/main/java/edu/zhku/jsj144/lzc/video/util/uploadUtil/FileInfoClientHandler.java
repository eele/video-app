package edu.zhku.jsj144.lzc.video.util.uploadUtil;

import android.util.Log;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.IOException;

public class FileInfoClientHandler extends SimpleChannelInboundHandler<Info> {
	
	private String uid;
    private String localFilepath;
    private String vid;

	public FileInfoClientHandler(String uid, String localFilepath, String vid) {
		this.uid = uid;
        this.localFilepath = localFilepath;
        this.vid = vid;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Info msg) {
		ctx.pipeline().remove(this);
		ctx.pipeline().remove("encoder");
		ctx.pipeline().remove("decoder");
		ctx.pipeline().addLast("streamer", new ChunkedWriteHandler());
		ctx.pipeline().addLast("handler", new UploadClientHandler((Info) msg, localFilepath));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws IOException {
		Info info = new Info("FILEINFO");
		info.setUid(uid);
		info.setVid(vid);
		info.setTotalsize(new File(localFilepath).length());
		ctx.writeAndFlush(info);
	}

}
