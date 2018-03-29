package edu.zhku.jsj144.lzc.video.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.util.WebUtil;

public class SubscribePageFragment extends Fragment {

    private Activity activity;
    private WebView webView;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_page_subscribe, container, false);

        webView = (WebView) rootView.findViewById(R.id.subscribeWebview);
        webView.addJavascriptInterface(new WebUtil(activity), "android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(BaseApplication.PAGE_BASE_URL + "/subscribe?r=all");

        return rootView;
    }
}
