package edu.zhku.jsj144.lzc.video.util;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import edu.zhku.jsj144.lzc.video.activity.UploadProcessingActivity;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;

public class WebUtil {

    private Context context;

    public WebUtil(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public String getCurrentUID() {
        return SharedPreferencesUtil.getString(context, "uid", "");
    }

    @JavascriptInterface
    public void promptUploadSuccess() {
        Toast.makeText(context, "一个视频已成功上传", Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void pauseUpload() {
        context.stopService(BaseApplication.getUploadIntent());
    }

    @JavascriptInterface
    public void resumeUpload(String vid) {
        BaseApplication.getUploadIntent()
                .putExtra("path", SharedPreferencesUtil.getString(context, "vid" + vid, ""));
        BaseApplication.getUploadIntent().putExtra("vid", vid);

        System.out.println("aaa" + SharedPreferencesUtil.getString(context, "vid" + vid, ""));
        System.out.println(vid);
        context.startService(BaseApplication.getUploadIntent());
    }

    @JavascriptInterface
    public void openMyVideo() {
        Intent intent = new Intent(context, UploadProcessingActivity.class);
        context.startActivity(intent);
    }
}
