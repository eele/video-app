package edu.zhku.jsj144.lzc.video.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import edu.zhku.jsj144.lzc.video.R;

public class PlayActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.floatingBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.this.finish();
            }
        });

        videoView = (VideoView) findViewById(R.id.videoView);
        String uri = "http://192.168.0.149/a/VID_20180226_210856.mp4";
        videoView.setVideoURI(Uri.parse(uri));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("aa",videoView.getWidth() + "  " + videoView.getHeight());
            }
        });
        videoView.start();//播放视频
        MediaController medis = new MediaController(PlayActivity.this);//显示控制条
        videoView.setMediaController(medis);
        medis.setMediaPlayer(videoView);//设置控制的对象  
        medis.show();
    }

}
