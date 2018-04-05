package edu.zhku.jsj144.lzc.video.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import edu.zhku.jsj144.lzc.video.util.NotificationUtil;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;
import edu.zhku.jsj144.lzc.video.util.UploadXmlUtil;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;
import net.grandcentrix.tray.core.ItemNotFoundException;

public class UploadService extends Service {

    private UploadAsyncTask asyncTask;
    private String vid;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationUtil.showUploadNotifiction();
        String videoPath = intent.getStringExtra("path");
        vid = intent.getStringExtra("vid");
        UploadClient.setUid(SharedPreferencesUtil.getString(this, "uid", "null"));
        asyncTask = new UploadAsyncTask(videoPath,vid);
        asyncTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationUtil.cancelUploadNotifiction();
        asyncTask.cancel(true);
        try {
            if (BaseApplication.getAppPreferences().getInt("progress") == 100) {
                BaseApplication.getAppPreferences().put("progress", -1);
                UploadXmlUtil.removeUploadingVideo(BaseApplication.getContext(), vid);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), "一个视频已成功上传", Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                            @Override
                            public void onViewDetachedFromWindow(View v) {
                                System.exit(0);
                            }

                            @Override
                            public void onViewAttachedToWindow(View v) {
                            }
                        });
                        toast.show();
                    }
                });
            } else {
                System.exit(0);
            }
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
    }

}
