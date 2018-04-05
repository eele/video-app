package edu.zhku.jsj144.lzc.video.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import edu.zhku.jsj144.lzc.video.interceptor.AuthInterceptor;
import edu.zhku.jsj144.lzc.video.service.UploadService;
import net.grandcentrix.tray.AppPreferences;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class BaseApplication extends Application {
    private static Context context;
    private static Intent uploadIntent;
    private static AppPreferences appPreferences;
    public static final String REST_BASE_URL = "http://192.168.0.149:8080/video/service/r";
    public static final String UPLOAD_BASE_IP = "192.168.0.149";
    public static final String PLAY_BASE_URL = "http://192.168.0.149";
    public static final String PAGE_BASE_URL = "http://192.168.0.149:8081";

    // 全局认证拦截器
    private static AuthInterceptor authInterceptor = new AuthInterceptor();

    public static AuthInterceptor getAuthInterceptor() {
        return authInterceptor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        uploadIntent = new Intent(context, UploadService.class);
        appPreferences = new AppPreferences(context);

        // 初始化网络请求
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 全局认证拦截器
        builder.addInterceptor(authInterceptor);
        //全局的读取超时时间
        builder.readTimeout(5000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(5000, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(5000, TimeUnit.MILLISECONDS);

        OkGo.getInstance().init(this) //必须调用初始化
                .setOkHttpClient(builder.build()) //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE) //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0); //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }

    public static Context getContext(){
        return  context;
    }

    public static Intent getUploadIntent() {
        return uploadIntent;
    }

    public static AppPreferences getAppPreferences() {
        return appPreferences;
    }
}
