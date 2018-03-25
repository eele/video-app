package edu.zhku.jsj144.lzc.video.activity;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.util.UnitUtil;
import edu.zhku.jsj144.lzc.video.util.WebUtil;

public class PlayActivity extends AppCompatActivity {

    private UniversalVideoView mVideoView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        final FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.floatingBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.this.finish();
            }
        });

        final View mBottomLayout = findViewById(R.id.media_controller);
        final View mVideoLayout = findViewById(R.id.video_layout);
        UniversalMediaController mMediaController;

        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        String uri = BaseApplication.PLAY_BASE_URL + "/"
                + getIntent().getStringExtra("uid") + "/"
                + getIntent().getStringExtra("vid")
                + "_/play.m3u8";
        mVideoView.setVideoURI(Uri.parse(uri));
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);

        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {

            @Override
            public void onScaleChange(boolean isFullscreen) {
                if (isFullscreen) {
                    backButton.setVisibility(View.GONE);
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoLayout.setLayoutParams(layoutParams);
                    //GONE the unconcerned views to leave room for video and controller
                    mBottomLayout.setVisibility(View.GONE);
                } else {
                    backButton.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = UnitUtil.dip2px(PlayActivity.this, 260);
                    mVideoLayout.setLayoutParams(layoutParams);
                    mBottomLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d("", "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d("", "onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d("", "onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d("", "onBufferingEnd UniversalVideoView callback");
            }

        });
        mVideoView.start();//播放视频

        webView = (WebView) findViewById(R.id.commentWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebUtil(PlayActivity.this, webView), "android");
        webView.loadUrl(BaseApplication.PAGE_BASE_URL + "/comment?vid=" + getIntent().getStringExtra("vid"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();//播放视频
    }
}
