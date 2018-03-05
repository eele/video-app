package edu.zhku.jsj144.lzc.video.service;

import android.os.AsyncTask;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;

public class UploadAsyncTask extends AsyncTask {

    private String videoPath;
    private String vid;

    public UploadAsyncTask(String videoPath, String vid) {
        this.videoPath = videoPath;
        this.vid = vid;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            UploadClient.startUpload(videoPath, vid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
