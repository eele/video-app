package edu.zhku.jsj144.lzc.video.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import edu.zhku.jsj144.lzc.video.activity.PlayActivity;
import edu.zhku.jsj144.lzc.video.activity.UploadProcessingActivity;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.interceptor.handler.AuthHandler;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;

public class WebUtil {

    private Context context;
    private WebView webView;
    private AuthHandler authHandler = new AuthHandler() {
        @Override
        public void onLoadingStart() {

        }

        @Override
        public void onLoadingEnd() {

        }
    };

    public WebUtil(Context context) {
        BaseApplication.getAuthInterceptor().setAuthHandler(authHandler);
        this.context = context;
    }

    public WebUtil(Context context, WebView webView) {
        BaseApplication.getAuthInterceptor().setAuthHandler(authHandler);
        this.context = context;
        this.webView = webView;
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
        context.startService(BaseApplication.getUploadIntent());
    }

    @JavascriptInterface
    public void openMyVideo() {
        Intent intent = new Intent(context, UploadProcessingActivity.class);
        intent.putExtra("startUpload", "0");
        context.startActivity(intent);
    }

    @JavascriptInterface
    public long getUploadProgress() {
        return UploadClient.getUploadProgress();
    }

    @JavascriptInterface
    public void play(String uid, String vid) {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("vid", vid);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void comment(final String vid) {
        final EditText editText = new EditText(context);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HttpParams params = new HttpParams();
                params.put("uid", SharedPreferencesUtil.getString(context, "uid", "null"));
                params.put("vid", vid);
                params.put("text", editText.getText().toString());
                OkGo.<String>post(BaseApplication.REST_BASE_URL + "/comments")
                        .params(params)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                webView.reload();
                            }
                        });
            }
        };
        new AlertDialog.Builder(context).setTitle("评论该视频").setIcon(
                android.R.drawable.ic_dialog_info).setView(editText).setPositiveButton("发表", listener)
                .setNegativeButton("取消", null).show();
    }
}
