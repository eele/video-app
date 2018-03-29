package edu.zhku.jsj144.lzc.video.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.util.WebUtil;

public class FavoritesActivity extends InterceptorActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.uploadToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoritesActivity.this.finish();
            }
        });

        webView = (WebView) findViewById(R.id.favoriteWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebUtil(FavoritesActivity.this), "android");
        webView.loadUrl(BaseApplication.PAGE_BASE_URL + "/myfavorites");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
