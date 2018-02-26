package edu.zhku.jsj144.lzc.video.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.util.UnitUtil;
import edu.zhku.jsj144.lzc.video.util.VideoInfo;
import edu.zhku.jsj144.lzc.video.util.VideoInfoUtil;

import java.io.File;
import java.util.List;

public class UploadProcessingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_processing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.uploadToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadProcessingActivity.this.finish();
            }
        });

    }

}
