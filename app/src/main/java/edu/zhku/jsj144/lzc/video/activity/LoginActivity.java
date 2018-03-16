package edu.zhku.jsj144.lzc.video.activity;

import android.animation.*;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.interceptor.JellyInterpolator;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mClose;
    private TextView mRegBtn;
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;

    private final MediaType FORM
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private String url = "http://192.168.0.149:8080/video/service/r/sessions";

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

        mClose.setOnClickListener(this);
        mRegBtn.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 点击登录按钮
        if (v == mBtnLogin) {
            mWidth = mBtnLogin.getMeasuredWidth();
            mHeight = mBtnLogin.getMeasuredHeight();

            mName.setVisibility(View.INVISIBLE);
            mPsw.setVisibility(View.INVISIBLE);

            EditText usernameEditText = (EditText) findViewById(R.id.input_edit_name);
            EditText passwordEditText = (EditText) findViewById(R.id.input_edit_psw);
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            mName.removeView(usernameEditText);
            mPsw.removeView(passwordEditText);

            inputAnimator(mInputLayout, mWidth, mHeight);

            // 开始登录认证
            authenticate(username, password);
        }
        // 点击关闭按钮
        if (v == mClose) {
            finish();
        }
        // 点击注册按钮
        if (v == mRegBtn) {

        }
    }

    private void authenticate(String username, String password) {
        RequestBody body = RequestBody.create(FORM, "username=" + username + "&password=" + password);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
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
                    finish();
                } else {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,
                            (String) info.get("stateMsg"), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

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

    }
}
