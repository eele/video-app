package edu.zhku.jsj144.lzc.video.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.activity.LoginActivity;
import edu.zhku.jsj144.lzc.video.activity.UploadChoiceActivity;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;
import edu.zhku.jsj144.lzc.video.util.WebUtil;

import java.io.IOException;
import java.util.Map;

public class MinePageFragment extends Fragment {

    private Activity activity;
    private WebView webView;
    private TextView usernameView;
    private ImageView setting;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_page_mine, container, false);

        Button uploadButton = (Button) rootView.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UploadChoiceActivity.class);
                startActivity(intent);
            }
        });
        usernameView = rootView.findViewById(R.id.usernameView);

        setting = rootView.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button logout = new Button(activity);
                logout.setText("退出登录");
                final Dialog dialog = new AlertDialog.Builder(activity).setTitle("设置").setIcon(
                        android.R.drawable.ic_dialog_info).setView(logout)
                        .setNegativeButton("关闭", null).show();

                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesUtil.remove(activity, "token");
                        SharedPreferencesUtil.remove(activity, "uid");
                        setting.setVisibility(View.GONE);
                        usernameView.setText("登录/注册");
                        usernameView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.dismiss();
                    }
                });
            }
        });

        webView = (WebView) rootView.findViewById(R.id.mineWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebUtil(activity), "android");
        webView.loadUrl(BaseApplication.PAGE_BASE_URL + "/mine");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (SharedPreferencesUtil.getString(activity, "uid", "").equals("")) {
            setting.setVisibility(View.GONE);
            usernameView.setText("登录/注册");
            usernameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            HttpParams params = new HttpParams();
            OkGo.<String>get(BaseApplication.REST_BASE_URL + "/users/" + SharedPreferencesUtil.getString(activity, "uid", ""))
                    .params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Map<String, Object> user = null;
                            try {
                                user = new ObjectMapper().readValue(response.body(), Map.class);
                                usernameView.setText((String) user.get("username"));
                                setting.setVisibility(View.VISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
