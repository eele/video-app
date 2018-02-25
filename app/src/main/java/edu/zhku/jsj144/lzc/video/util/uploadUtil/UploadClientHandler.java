package edu.zhku.jsj144.lzc.video.util.uploadUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.io.RandomAccessFile;

public class UploadClientHandler extends SimpleChannelInboundHandler<String> {

	private RandomAccessFile raf = null;
	private long totalLength = -1;
	private Info info;
	private String localFilepath;

	public UploadClientHandler(Info info, String localFilepath) {
		this.info = info;
		this.localFilepath = localFilepath;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws IOException {
		try {
			raf = new RandomAccessFile(localFilepath, "r");
			totalLength = raf.length();
			ctx.write(new DefaultFileRegion(raf.getChannel(), info.getFinishedSize(), totalLength));
			ctx.writeAndFlush("\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (totalLength < 0 && raf != null) {
				raf.close();
			}
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) {
		// TODO Auto-generated method stub
	}

}
