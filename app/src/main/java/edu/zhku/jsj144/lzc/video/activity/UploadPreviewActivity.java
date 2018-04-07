package edu.zhku.jsj144.lzc.video.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.dialog.CustomProgressDialog;
import edu.zhku.jsj144.lzc.video.interceptor.handler.AuthHandler;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;
import edu.zhku.jsj144.lzc.video.util.UploadXmlUtil;
import edu.zhku.jsj144.lzc.video.util.VideoPlayerIJK;
import edu.zhku.jsj144.lzc.video.util.VideoPlayerListener;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class UploadPreviewActivity extends AppCompatActivity {

    private VideoPlayerIJK ijkPlayer;
    private SeekBar seekBar;
    private Timer timer = new Timer();
    private String url = BaseApplication.REST_BASE_URL + "/videos";

    private CustomProgressDialog dialog;
    private TextView category;
    private EditText titleEditText;
    private EditText descEditText;

    private String cid = "";

    private AuthHandler authHandler = new AuthHandler() {
        @Override
        public void onLoadingStart() {
            // 加载提示
            if (dialog != null) {
                dialog.show();
            }
        }

        @Override
        public void onLoadingEnd() {
            // 解除加载提示
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
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

        category = (TextView) findViewById(R.id.v_category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategory();
            }
        });
        descEditText = (EditText) findViewById(R.id.v_desc);

        // 显示默认视频标题
        String videoTitle = getIntent().getStringExtra("title");
        titleEditText = (EditText) findViewById(R.id.v_title);
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

        timer.schedule(new SeekTimeTask(), new Date(), 100);

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
        if (titleEditText.getText().toString().length() == 0 || cid.length() == 0) {
            Toast.makeText(UploadPreviewActivity.this, "标题与分类不为空", Toast.LENGTH_LONG).show();
        } else {
            HttpParams params = new HttpParams();
            params.put("title", titleEditText.getText().toString());
            params.put("uid", SharedPreferencesUtil.getString(this, "uid", ""));
            params.put("cid", cid);
            params.put("description", descEditText.getText().toString());
            OkGo.<String>post(url)
                    .params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                // 设置上传信息
                                Map<String, Object> vidData = new ObjectMapper().readValue(response.body(), Map.class);
                                SharedPreferencesUtil.putString(BaseApplication.getContext(),
                                        "vid" + (String) vidData.get("id"),
                                        getIntent().getStringExtra("path"));
                                UploadXmlUtil.addUploadingVideo(UploadPreviewActivity.this,
                                        (String) vidData.get("id"), "视频题目",
                                        String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()),
                                        getIntent().getStringExtra("path"));

                                // 打开上传视频列表
                                UploadPreviewActivity.this.setResult(RESULT_OK, null);
                                UploadPreviewActivity.this.finish();
                                Intent intent = new Intent(UploadPreviewActivity.this, UploadProcessingActivity.class);
                                intent.putExtra("uploadingVideoID", (String) vidData.get("id"));
                                startActivity(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * 进度条定时
     */
    private class SeekTimeTask extends TimerTask {
        public void run() {
            if (ijkPlayer != null) {
                seekBar.setProgress((int) ijkPlayer.getCurrentPosition());
            }
        }
    }

    private void getCategory() {
        OkGo.<String>get(BaseApplication.REST_BASE_URL + "/categories?name=")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            final List<Map<String, Object>> categories = new ObjectMapper()
                                    .readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
                            final String[] items = new String[categories.size()];
                            int i = 0;
                            for (Map<String, Object> map: categories) {
                                items[i] = (String) map.get("name");
                                i++;
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(UploadPreviewActivity.this);
                            builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("选择分类")
                                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            cid = (String) ((Map<String, Object>) categories.get(which)).get("id");
                                            category.setText(items[which]);
                                            category.setTextColor(Color.BLACK);
                                        }
                                    });
                            builder.create().show();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
