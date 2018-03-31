package edu.zhku.jsj144.lzc.video.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.util.MD5Util;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;

import java.io.IOException;

public class RegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        Button regButton = (Button) findViewById(R.id.regButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText cpassword = (EditText) findViewById(R.id.cpassword);
        final EditText desc = (EditText) findViewById(R.id.desc);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getEditableText().length() == 0
                        && password.getEditableText().length() == 0
                        && cpassword.getEditableText().length() == 0) {
                    Toast.makeText(RegActivity.this, "用户名、密码、确认密码不为空", Toast.LENGTH_LONG).show();
                } else if (!password.getEditableText().toString().equals(cpassword.getEditableText().toString())) {
                    Toast.makeText(RegActivity.this, "新建密码与确认密码不一致", Toast.LENGTH_LONG).show();
                } else {
                    HttpParams params = new HttpParams();
                    params.put("username", username.getEditableText().toString());
                    params.put("password", MD5Util.md5Password(password.getEditableText().toString()));
                    params.put("description", desc.getEditableText().toString());
                    OkGo.<String>post(BaseApplication.REST_BASE_URL + "/users")
                            .params(params)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                }
            }
        });
    }
}
