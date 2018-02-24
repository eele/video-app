package edu.zhku.jsj144.lzc.video.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.util.UnitUtil;
import edu.zhku.jsj144.lzc.video.util.VideoInfo;
import edu.zhku.jsj144.lzc.video.util.VideoInfoUtil;

import java.io.File;
import java.util.List;

public class UploadPreviewActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_preview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.uploadToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPreviewActivity.this.finish();
            }
        });

        // 视频地址
        String videoPath = getIntent().getStringExtra("path");

        videoView = (VideoView)this.findViewById(R.id.previewVideoView );
        // 设置视频控制器
        videoView.setMediaController(new MediaController(this));
        // 播放完成回调
        videoView.setOnCompletionListener( new PlayerOnCompletionListener());
        // 设置视频路径
        videoView.setVideoPath(videoPath);
        // 开始播放视频
        videoView.start();
    }

    class PlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            //Toast.makeText( LocalVideoActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}
