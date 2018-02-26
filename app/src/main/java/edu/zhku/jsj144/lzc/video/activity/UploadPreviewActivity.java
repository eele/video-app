package edu.zhku.jsj144.lzc.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.util.VideoPlayerIJK;
import edu.zhku.jsj144.lzc.video.util.VideoPlayerListener;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class UploadPreviewActivity extends AppCompatActivity {

    private VideoPlayerIJK ijkPlayer;
    private SeekBar seekBar;
    private SeekThread seekThread = new SeekThread();

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
                UploadPreviewActivity.this.setResult(RESULT_OK, null);
                UploadPreviewActivity.this.finish();
                Intent intent = new Intent(UploadPreviewActivity.this, UploadProcessingActivity.class);
                intent.putExtra("path", videoPath);
                intent.putExtra("vid", "123");
                intent.putExtra("uid", "aa");
                startActivity(intent);

//                new Thread() {
//                    @Override
//                    public void run() {
//
//                        try {
//                            UploadClient.setUid("aa");
//                            UploadClient.startUpload(videoPath, "123");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Looper.prepare();
//                            Toast.makeText(
//                                    UploadPreviewActivity.this,
//                                    "上传失败", Toast.LENGTH_LONG).show();
//                            Looper.loop();
//                        }
//                    }
//                }.start();
            }
        });

        //加载so文件
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }

        ijkPlayer = (VideoPlayerIJK) findViewById(R.id.previewVideoView);
        ijkPlayer.setVideoPath(videoPath);
        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {}

            @Override
            public void onCompletion(IMediaPlayer mp) {
                mp.seekTo(0);
                seekThread.setPause();
                playButton.setImageResource(R.drawable.ic_action_play);
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
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
                            seekThread.setPause();
                        } else {
                            playButton.setImageResource(R.drawable.ic_action_pause);
                            mp.start();
                            seekThread.setStart();
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
                seekThread.start();
                mp.start();
            }

            @Override
            public void onSeekComplete(IMediaPlayer mp) {}

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {}
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        seekThread.setStop();
        ijkPlayer.stop();
        IjkMediaPlayer.native_profileEnd();
    }

    private class SeekThread extends Thread {

        private boolean stop = false;
        private boolean pause = false;

        public void setStop() {
            stop = true;
        }

        public void setPause() {
            pause = true;
        }

        public void setStart() {
            pause = false;
        }

        @Override
        public void run() {
            while (stop == false) {
                try {
                    if (pause == false) {
                        seekBar.setProgress((int) ijkPlayer.getCurrentPosition());
                    }
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stop = false;
        }
    }

}
