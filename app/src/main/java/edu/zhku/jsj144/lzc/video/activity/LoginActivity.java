package edu.zhku.jsj144.lzc.video.activity;

import android.animation.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.interceptor.JellyInterpolator;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final MediaType FORM
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private ImageView mClose;
    private TextView mRegBtn;
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private OkHttpClient client = new OkHttpClient();
    private String url = "http://192.168.0.149:8080/video/service/r/sessions";

    private String username;
    private String password;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                // 恢复显示输入框和登录按钮
                mName.setVisibility(View.VISIBLE);
                mPsw.setVisibility(View.VISIBLE);
                mName.addView(usernameEditText);
                mPsw.addView(passwordEditText);
                progress.setVisibility(View.INVISIBLE);
                mInputLayout.setVisibility(View.VISIBLE);
                mBtnLogin.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout
                        .getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.leftMargin = 0;
                params.rightMargin = 0;
                mInputLayout.setLayoutParams(params);

                // 恢复输入框原始大小
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator animator = ObjectAnimator.ofFloat(mInputLayout,
                        "scaleX", 0.5f, 1f);
                set.setDuration(300);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.playTogether(animator);
                set.start();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        mClose = (ImageView) findViewById(R.id.login_close);
        mRegBtn = (TextView) findViewById(R.id.reg_btn);
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        usernameEditText = (EditText) findViewById(R.id.input_edit_name);
        passwordEditText = (EditText) findViewById(R.id.input_edit_psw);

        mClose.setOnClickListener(this);
        mRegBtn.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 点击登录按钮
        if (v == mBtnLogin) {
            // 检查输入框
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            if (username.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this,
                        "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            } else {
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();

                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                mName.removeView(usernameEditText);
                mPsw.removeView(passwordEditText);
                mBtnLogin.setVisibility(View.INVISIBLE);

                // 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);

                inputAnimator(mInputLayout, mWidth, mHeight);
            }
        }
        // 点击关闭按钮
        if (v == mClose) {
            finish();
        }
        // 点击注册按钮
        if (v == mRegBtn) {

        }
    }

    /**
     * 登陆认证
     *
     * @param username
     * @param password
     */
    private void authenticate(final String username, final String password) {
        RequestBody body = RequestBody.create(FORM, "username=" + username + "&password=" + password);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0); // 恢复显示输入框和登录按钮

                Looper.prepare();
                Toast.makeText(
                        LoginActivity.this,
                        "连接异常", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Map<String, Object> info = new ObjectMapper().readValue(response.body().string(), Map.class);
                if (info.get("stateMsg").equals("OK")) {
                    // 获取SharedPreferences对象
                    SharedPreferences sharedPre =
                            LoginActivity.this.getSharedPreferences("config", LoginActivity.this.MODE_PRIVATE);
                    // 获取Editor对象
                    SharedPreferences.Editor editor = sharedPre.edit();
                    // 保存用户名、密码和令牌
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("token", (String) info.get("token"));
                    // 提交保存
                    editor.commit();
                    // 关闭登录界面
                    LoginActivity.this.finish();
                } else {
                    handler.sendEmptyMessage(0); // 恢复显示输入框和登录按钮

                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,
                            (String) info.get("stateMsg"), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    /**
     * 输入框动画
     *
     * @param view
     * @param w
     * @param h
     */
    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(800);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 开始登录认证
                authenticate(username, password);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
