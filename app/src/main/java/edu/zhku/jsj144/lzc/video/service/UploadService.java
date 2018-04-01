package edu.zhku.jsj144.lzc.video.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import edu.zhku.jsj144.lzc.video.util.SharedPreferencesUtil;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;

public class UploadService extends Service {

    private UploadAsyncTask asyncTask;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String videoPath = intent.getStringExtra("path");
        String vid = intent.getStringExtra("vid");
        UploadClient.setUid(SharedPreferencesUtil.getString(this, "uid", "null"));
        asyncTask = new UploadAsyncTask(videoPath,vid);
        asyncTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        asyncTask.cancel(true);
    }
}
