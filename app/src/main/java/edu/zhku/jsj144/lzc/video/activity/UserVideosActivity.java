package edu.zhku.jsj144.lzc.video.activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;
import edu.zhku.jsj144.lzc.video.util.WebUtil;

import java.io.IOException;
import java.util.Map;

public class UserVideosActivity extends InterceptorActivity {

    private WebView webView;
    private MenuItem menuItem;

    private String subscribeID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_videos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserVideosActivity.this.finish();
            }
        });
        toolbar.inflateMenu(R.menu.subscribe);
        menuItem = toolbar.getMenu().getItem(0);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (subscribeID.equals("")) {
                    subscribe();
                } else {
                    unSubscribe();
                }
                return true;
            }
        });

        getSubscribe();

        webView = (WebView) findViewById(R.id.userVideosWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebUtil(UserVideosActivity.this), "android");
        webView.loadUrl(BaseApplication.PAGE_BASE_URL + "/uservideos?uid=" + getIntent().getStringExtra("uid"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    private void subscribe() {
        HttpParams params = new HttpParams();
        params.put("uid", SharedPreferencesUtil.getString(UserVideosActivity.this, "uid", "null"));
        params.put("s_uid", getIntent().getStringExtra("uid"));
        OkGo.<String>post(BaseApplication.REST_BASE_URL + "/subscribe")
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Toast.makeText(BaseApplication.getContext(), "已订阅", Toast.LENGTH_LONG).show();
                        menuItem.setTitle("取消订阅");
                        try {
                            Map<String, Object> info = new ObjectMapper().readValue(response.body(), Map.class);
                            subscribeID = (String) info.get("id");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void unSubscribe() {
        HttpParams params = new HttpParams();
        OkGo.<String>delete(BaseApplication.REST_BASE_URL + "/subscribe/" + subscribeID)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Toast.makeText(BaseApplication.getContext(), "已取消订阅", Toast.LENGTH_LONG).show();
                        menuItem.setTitle("订阅");
                        subscribeID = "";
                    }
                });
    }

    private void getSubscribe() {
        HttpParams params = new HttpParams();
        params.put("uid", SharedPreferencesUtil.getString(UserVideosActivity.this, "uid", "null"));
        params.put("s_uid", getIntent().getStringExtra("uid"));
        OkGo.<String>get(BaseApplication.REST_BASE_URL + "/subscribe")
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        subscribeID = response.body();
                        if (subscribeID.equals("")) {
                            menuItem.setTitle("订阅");
                        } else {
                            menuItem.setTitle("取消订阅");
                        }
                    }
                });
    }
}
