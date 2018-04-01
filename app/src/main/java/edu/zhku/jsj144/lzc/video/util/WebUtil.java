package edu.zhku.jsj144.lzc.video.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import edu.zhku.jsj144.lzc.video.activity.*;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.interceptor.handler.AuthHandler;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    public void pauseUpload(String vid, String progress) {
        context.stopService(BaseApplication.getUploadIntent());
        SharedPreferencesUtil.putString(context, "p-" + vid, progress);
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
        if (!hasLogin()) {
            return;
        }
        Intent intent = new Intent(context, UploadProcessingActivity.class);
        intent.putExtra("uploadingVideoID", "0");
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openMyFavorite() {
        if (!hasLogin()) {
            return;
        }
        Intent intent = new Intent(context, FavoritesActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openUserVideoList(String uid) {
        Intent intent = new Intent(context, UserVideosActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openMySubscribe() {
        if (!hasLogin()) {
            return;
        }
        Intent intent = new Intent(context, SubscribeActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openMyHistory() {
        if (!hasLogin()) {
            return;
        }
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public long getUploadProgress() {
        return UploadClient.getUploadProgress();
    }

    @JavascriptInterface
    public int getSavedUploadProgress(String vid) {
        return Integer.parseInt(SharedPreferencesUtil.getString(context, "p-" + vid, "0"));
    }

    @JavascriptInterface
    public void removeSavedUploadProgress(String vid) {
        SharedPreferencesUtil.remove(context, "p-" + vid);
        context.stopService(BaseApplication.getUploadIntent());
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

    @JavascriptInterface
    public void setCollect(String vid) {
        HttpParams params = new HttpParams();
        params.put("uid", SharedPreferencesUtil.getString(context, "uid", "null"));
        params.put("vid", vid);
        OkGo.<String>post(BaseApplication.REST_BASE_URL + "/favorites")
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        webView.loadUrl("javascript:getFavoriteID()");
                    }
                });
    }

    @JavascriptInterface
    public void setUnCollect(String fid) {
        HttpParams params = new HttpParams();
        OkGo.<String>delete(BaseApplication.REST_BASE_URL + "/favorites/" + fid)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        webView.loadUrl("javascript:getFavoriteID()");
                    }
                });
    }

    @JavascriptInterface
    public String getHistoryVid(int start, int size) throws IOException {
        String vidJson = "[]";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/vi/" + SharedPreferencesUtil.getString(context, "uid", ""));
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        try {
            List<String> vidList = new ArrayList<String>();
            int line = 0;
            String vid = br.readLine();
            while (vid != null) {
                if (line >= start) {
                    vidList.add(vid);
                }
                line++;
                if (line > start + size) {
                    break;
                }
                vid = br.readLine();
            }
            ObjectMapper mapper = new ObjectMapper();
            vidJson = mapper.writeValueAsString(vidList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return vidJson;
    }

    @JavascriptInterface
    public void deleteVideoItem(String id) {
        OkGo.<String>delete(BaseApplication.REST_BASE_URL + "/videos/" + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        webView.post(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("javascript:refresh()");
                            }
                        });
                    }
                });
    }

    @JavascriptInterface
    public void deleteHistoryItem(String id) throws IOException {
        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/vi/" + SharedPreferencesUtil.getString(context, "uid", ""));
        File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/vi/tmp" + SharedPreferencesUtil.getString(context, "uid", ""));
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2)));
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
            String vid = br.readLine();
            while (vid != null) {
                if (! vid.equals(id)) {
                    bw.write(vid);
                    bw.newLine();
                }
                vid = br.readLine();
            }
            file1.delete();
            file2.renameTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw.close();
            br.close();
        }
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:refresh()");
            }
        });
    }

    @JavascriptInterface
    public void cancelFavoriteItem(String id) {
        HttpParams params = new HttpParams();
        params.put("uid", SharedPreferencesUtil.getString(context, "uid", ""));
        params.put("vid", id);
        OkGo.<String>delete(BaseApplication.REST_BASE_URL + "/favorites")
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        webView.post(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("javascript:refresh()");
                            }
                        });
                    }
                });
    }

    private boolean hasLogin() {
        String uid = SharedPreferencesUtil.getString(context, "uid", "");
        String token = SharedPreferencesUtil.getString(context, "token", "");
        if (uid.equals("") || token.equals("")) {
            // 要求重新登录
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.getContext().startActivity(intent);
            return false;
        }
        return true;
    }
}
