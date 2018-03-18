package edu.zhku.jsj144.lzc.video.interceptor.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.zhku.jsj144.lzc.video.activity.LoginActivity;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

public abstract class AuthHandler extends Handler {

    private Response response;
    private Intent intent = new Intent(BaseApplication.getContext(), LoginActivity.class);

    public abstract void onLoadingStart();

    public abstract void onLoadingEnd();

    public void onTokenError() {
        // 要求重新登录
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.getContext().startActivity(intent);
    }

    public void onServerError(Response response) throws IOException {
        // 提示服务端异常信息
        Map<String, Object> info = new ObjectMapper().readValue(response.body().string(), Map.class);
        Toast.makeText(BaseApplication.getContext(),
                (String) info.get("msg"), Toast.LENGTH_LONG).show();
    }

    public void onOtherHttpError(Response response) throws IOException {
        // 提示其他HTTP异常信息
        Toast.makeText(BaseApplication.getContext(), "访问异常", Toast.LENGTH_LONG).show();
    }

    public void onConnectionError(Response response) throws IOException {
        // 提示连接异常信息
        Toast.makeText(BaseApplication.getContext(), "连接异常", Toast.LENGTH_LONG).show();
    }

    public void doAfterLoadingStart() {
        sendEmptyMessage(1);
    }

    public void doAfterLoadingEnd() {
        sendEmptyMessage(2);
    }

    public void doAfterTokenError(Response response) {
        this.response = response;
        sendEmptyMessage(3);
    }

    public void doAfterServerError(Response response) {
        this.response = response;
        sendEmptyMessage(4);
    }

    public void doAfterOtherHttpError(Response response) {
        this.response = response;
        sendEmptyMessage(5);
    }

    public void doAfterConnectionError() {
        sendEmptyMessage(6);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                onLoadingStart();
                break;
            case 2:
                onLoadingEnd();
                break;
            case 3:
                onLoadingEnd();
                onTokenError();
                break;
            case 4:
                try {
                    onLoadingEnd();
                    onServerError(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    onLoadingEnd();
                    onOtherHttpError(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    onLoadingEnd();
                    onConnectionError(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
