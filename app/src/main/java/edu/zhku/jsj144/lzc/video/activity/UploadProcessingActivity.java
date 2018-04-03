package edu.zhku.jsj144.lzc.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.service.UploadService;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;
import edu.zhku.jsj144.lzc.video.util.WebUtil;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class UploadProcessingActivity extends InterceptorActivity {

    private WebView webView;

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

        webView = (WebView) findViewById(R.id.uploadWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebUtil(UploadProcessingActivity.this, webView), "android");
        webView.loadUrl(BaseApplication.PAGE_BASE_URL + "/myvideos?uploadingVideoID=" + getIntent().getStringExtra("uploadingVideoID"));
    }

}
