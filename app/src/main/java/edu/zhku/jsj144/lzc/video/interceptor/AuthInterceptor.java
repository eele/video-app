package edu.zhku.jsj144.lzc.video.interceptor;

import android.os.Looper;
import android.widget.Toast;
import com.lzy.okgo.OkGo;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.interceptor.handler.AuthHandler;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * 全局认证拦截器，对每次请求加上令牌，并在服务端提示令牌无效时要求重新登录
 */
public class AuthInterceptor implements Interceptor {

    private AuthHandler authHandler;

    public void setAuthHandler(AuthHandler authHandler) {
        this.authHandler = authHandler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (authHandler != null) {
            authHandler.doAfterLoadingStart();
        }

        // 将令牌添加到请求头
        Request request = chain.request().newBuilder()
                .addHeader("Auth-Token", SharedPreferencesUtil.getString(BaseApplication.getContext(),
                        "token", ""))
                .build();
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (SocketTimeoutException e) {
            if (authHandler != null) {
                authHandler.doAfterLoadingEnd();
                authHandler.doAfterConnectionError();
            }
            throw new IOException();
        }

        if (response.code() != 200 && response.code() != 204) {
            // 若服务端返回令牌无效信息
            if (response.code() == 403 && response.body().string().contains("TOKENERR")) {
                if (authHandler != null) {
                    authHandler.doAfterTokenError(response);
                }
            } else if (response.code() == 500) {
                if (authHandler != null) {
                    authHandler.doAfterServerError(response);
                }
            } else {
                if (authHandler != null) {
                    authHandler.doAfterOtherHttpError(response);
                }
            }
            throw new IOException();
        }
        if (authHandler != null) {
            authHandler.doAfterLoadingEnd();
        }

        return response;
    }
}
