package edu.zhku.jsj144.lzc.video.util;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public abstract class VideoPlayerListener implements
        IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnSeekCompleteListener {
}
