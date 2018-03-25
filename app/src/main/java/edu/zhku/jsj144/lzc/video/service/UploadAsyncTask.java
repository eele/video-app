package edu.zhku.jsj144.lzc.video.service;

import android.os.AsyncTask;
import edu.zhku.jsj144.lzc.video.util.uploadUtil.UploadClient;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class UploadAsyncTask extends AsyncTask {

    private String videoPath;
    private String vid;
    private Timer timer = new Timer();
    private TimerTask timerTask = new SeekTimeTask();

    public UploadAsyncTask(String videoPath, String vid) {
        this.videoPath = videoPath;
        this.vid = vid;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            timer.schedule(new SeekTimeTask(), new Date(), 1000);
            UploadClient.startUpload(videoPath, vid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if (UploadClient.getUploadProgress() == 100) {
            timer.cancel();
        }
        System.out.println(UploadClient.getUploadProgress());
    }

    private class SeekTimeTask extends TimerTask {
        public void run() {
            publishProgress();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        timer.cancel();
    }
}
