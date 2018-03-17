package edu.zhku.jsj144.lzc.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.dialog.CustomProgressDialog;
import edu.zhku.jsj144.lzc.video.interceptor.handler.AuthHandler;
import edu.zhku.jsj144.lzc.video.service.UploadService;
import edu.zhku.jsj144.lzc.video.util.VideoPlayerIJK;
import edu.zhku.jsj144.lzc.video.util.VideoPlayerListener;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UploadPreviewActivity extends AppCompatActivity {

    private VideoPlayerIJK ijkPlayer;
    private SeekBar seekBar;
    private Timer timer = new Timer();
    private String url = "http://192.168.0.149:8080/video/service/r/videos";

    private CustomProgressDialog dialog;

    private AuthHandler authHandler = new AuthHandler() {
        @Override
        public void onLoadingStart() {
            // 加载提示
            dialog.show();
        }

        @Override
        public void onLoadingEnd() {
            // 解除加载提示
            dialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_preview);

        BaseApplication.getAuthInterceptor().setAuthHandler(authHandler);
        dialog = new CustomProgressDialog(UploadPreviewActivity.this, R.style.CustomDialog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.uploadToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPreviewActivity.this.finish();
            }
        });
        final ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        seekBar = (SeekBar) findViewById(R.id.videoSeekBar);

        // 显示默认视频标题
        String videoTitle = getIntent().getStringExtra("title");
        EditText titleEditText = (EditText) findViewById(R.id.v_title);
        titleEditText.setText(videoTitle);

        // 视频地址
        final String videoPath = getIntent().getStringExtra("path");

        // 确认上传按钮
        Button uploadConfirmButton = (Button) findViewById(R.id.uploadConfirmButton);
        uploadConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postVideo();
            }
        });

        //加载so文件
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }

        timer.schedule(new SeekTimeTask(), 100);

        ijkPlayer = (VideoPlayerIJK) findViewById(R.id.previewVideoView);
        ijkPlayer.setVideoPath(videoPath);
        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                mp.seekTo(0);
                playButton.setImageResource(R.drawable.ic_action_play);
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
                    //这里返回了视频旋转的角度，根据角度旋转视频到正确的画面
//                    ijkPlayer.setRotation(extra);
                }
                return true;
            }

            @Override
            public void onPrepared(final IMediaPlayer mp) {
                playButton.setImageResource(R.drawable.ic_action_pause);
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mp.isPlaying()) {
                            playButton.setImageResource(R.drawable.ic_action_play);
                            mp.pause();
                        } else {
                            playButton.setImageResource(R.drawable.ic_action_pause);
                            mp.start();
                        }
                    }
                });

                // 进度条
                seekBar.setMax((int) mp.getDuration());
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    private boolean touch = false;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (touch == true) {
                            ijkPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        touch = true;
                        playButton.setImageResource(R.drawable.ic_action_play);
                        ijkPlayer.pause();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        touch = false;
                    }
                });
                seekBar.setProgress(0);
                mp.start();
            }

            @Override
            public void onSeekComplete(IMediaPlayer mp) {
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ijkPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        ijkPlayer.stop();
        IjkMediaPlayer.native_profileEnd();
    }

    private void postVideo() {
        HttpParams params = new HttpParams();
        params.put("title", "%E7%A4%BA%E4%BE%8B%E8%A7%86%E9%A2%91");
        params.put("uid", "c257c5ace0ff4b69bf889c0cb3cb4476");
        params.put("cid", "qw");
        params.put("description", "%E8%BF%99%E6%98%AF%E4%B8%80%E4%B8%AA%E8%A7%86%E9%A2%91");
        params.put("permission", "0");
        OkGo.<String>post(url)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            // 启动上传服务
                            Intent startIntent = new Intent(UploadPreviewActivity.this, UploadService.class);
                            Map<String, Object> vidData = new ObjectMapper().readValue(response.body(), Map.class);
                            startIntent.putExtra("path", getIntent().getStringExtra("path"));
                            startIntent.putExtra("vid", (String) vidData.get("id"));
                            startService(startIntent);

                            // 打开上传视频列表
                            UploadPreviewActivity.this.setResult(RESULT_OK, null);
                            UploadPreviewActivity.this.finish();
                            Intent intent = new Intent(UploadPreviewActivity.this, UploadProcessingActivity.class);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 进度条定时
     */
    private class SeekTimeTask extends TimerTask {
        public void run() {
            seekBar.setProgress((int) ijkPlayer.getCurrentPosition());
        }
    }

}
