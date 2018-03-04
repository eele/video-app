package edu.zhku.jsj144.lzc.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.service.UploadService;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class UploadProcessingActivity extends InterceptorActivity {

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
