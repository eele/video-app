package edu.zhku.jsj144.lzc.video.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.activity.FavoritesActivity;
import edu.zhku.jsj144.lzc.video.activity.LoginActivity;
import edu.zhku.jsj144.lzc.video.activity.UploadChoiceActivity;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.util.MD5Util;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;
import edu.zhku.jsj144.lzc.video.util.WebUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class MinePageFragment extends Fragment {

    private Activity activity;
    private WebView webView;
    private TextView usernameView;
    private ImageView setting;
    private ImageView portrait;

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

        portrait = rootView.findViewById(R.id.portrait);
        portrait.setBackgroundColor(Color.GRAY);
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        });
        setting = rootView.findViewById(R.id.setting);
        setting.setVisibility(View.GONE);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout settingLayout =(LinearLayout) activity.getLayoutInflater()
                        .inflate(R.layout.dialog_setting, null);
                Button changePwd = settingLayout.findViewById(R.id.changePwd);
                Button changeInfo = settingLayout.findViewById(R.id.changeInfo);
                Button logout = settingLayout.findViewById(R.id.logout);

                changeInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeInfo();
                    }
                });
                changePwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePwd();
                    }
                });

                final Dialog dialog = new AlertDialog.Builder(activity).setTitle("设置").setIcon(
                        android.R.drawable.ic_dialog_info)
                        .setView(settingLayout)
                        .setNegativeButton("关闭", null).show();

                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesUtil.remove(activity, "token");
                        SharedPreferencesUtil.remove(activity, "uid");
                        setting.setVisibility(View.GONE);
                        portrait.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        portrait.setImageBitmap(null);
                        portrait.setBackgroundColor(Color.GRAY);
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
            getPortrait();
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
                                usernameView.setOnClickListener(null);
                                setting.setVisibility(View.VISIBLE);
                                portrait.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(activity, FilePickerActivity.class);
                                        intent.putExtra(FilePickerActivity.ARG_FILTER, Pattern.compile(".*\\.(jpg|jpeg|png)$"));
                                        intent.putExtra(FilePickerActivity.ARG_TITLE, "请选择上传的头像");
                                        intent.putExtra(FilePickerActivity.ARG_START_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
                                        intent.putExtra(FilePickerActivity.ARG_CLOSEABLE, true);
                                        startActivityForResult(intent, 123);

//                                        new MaterialFilePicker()
//                                                .withActivity(activity)
//                                                .withTitle("请选择上传的头像")
//                                                .withRootPath(Environment.getExternalStorageDirectory().getAbsolutePath())
//                                                .withCloseMenu(true)
//                                                .withRequestCode(123)
//                                                .withFilterDirectories(true)
//                                                .start();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && data != null) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            HttpParams params = new HttpParams();
            params.put("file", new File(filePath));
            OkGo.<String>post(BaseApplication.REST_BASE_URL + "/users/" + SharedPreferencesUtil.getString(activity, "uid", "") + "/portrait")
                    .params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            getPortrait();
                            Toast.makeText(activity, "已上传新用户头像", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void getPortrait() {
        OkGo.<File>get(BaseApplication.REST_BASE_URL + "/users/"
                + SharedPreferencesUtil.getString(activity, "uid", "") + "/portrait")
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Bitmap bmp;
                        try {
                            bmp = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(response.body()));
                            portrait.setImageBitmap(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void changePwd() {
        LinearLayout cPwdLayout =(LinearLayout) activity.getLayoutInflater()
                .inflate(R.layout.dialog_changepwd, null);
        final EditText newPwd = cPwdLayout.findViewById(R.id.newPwd);
        final EditText conPwd = cPwdLayout.findViewById(R.id.conPwd);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (! newPwd.getText().toString().equals(conPwd.getText().toString())) {
                    Toast.makeText(activity, "新密码与确认密码不一致", Toast.LENGTH_LONG).show();
                } else {
                    HttpParams params = new HttpParams();
                    params.put("pwd", MD5Util.md5Password(newPwd.getText().toString()));
                    OkGo.<String>put(BaseApplication.REST_BASE_URL + "/users/" + SharedPreferencesUtil.getString(activity, "uid", "null") + "/password")
                            .params(params)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    Toast.makeText(activity, "密码修改成功", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        };
        new AlertDialog.Builder(activity).setTitle("修改密码").setIcon(
                android.R.drawable.ic_dialog_info)
                .setView(cPwdLayout)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null).show();
    }

    private void changeInfo() {
        LinearLayout cInfoLayout =(LinearLayout) activity.getLayoutInflater()
                .inflate(R.layout.dialog_changeinfo, null);
        final EditText username = cInfoLayout.findViewById(R.id.username);
        final EditText userdesc = cInfoLayout.findViewById(R.id.userdesc);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HttpParams params = new HttpParams();
                params.put("username", username.getText().toString());
                params.put("description", userdesc.getText().toString());
                OkGo.<String>put(BaseApplication.REST_BASE_URL + "/users/" + SharedPreferencesUtil.getString(activity, "uid", "null") + "/info")
                        .params(params)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                OkGo.<String>get(BaseApplication.REST_BASE_URL + "/users/" + SharedPreferencesUtil.getString(activity, "uid", ""))
                                        .execute(new StringCallback() {
                                                     @Override
                                                     public void onSuccess(Response<String> response) {
                                                         try {
                                                             Map<String, Object> user = new ObjectMapper().readValue(response.body(), Map.class);
                                                             usernameView.setText((String) user.get("username"));
                                                         } catch (JsonParseException e) {
                                                             e.printStackTrace();
                                                         } catch (JsonMappingException e) {
                                                             e.printStackTrace();
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }
                                                     }
                                                 });
                                Toast.makeText(activity, "个人信息修改成功", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        };
        new AlertDialog.Builder(activity).setTitle("修改个人信息").setIcon(
                android.R.drawable.ic_dialog_info)
                .setView(cInfoLayout)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null).show();
    }
}
