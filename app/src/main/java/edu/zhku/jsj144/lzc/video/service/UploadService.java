package edu.zhku.jsj144.lzc.video.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;

public class UploadService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            String videoPath = intent.getStringExtra("path");
            String vid = intent.getStringExtra("vid");
            UploadClient.startUpload(videoPath, vid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
