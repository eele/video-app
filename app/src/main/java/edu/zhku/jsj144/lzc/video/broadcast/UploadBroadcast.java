package edu.zhku.jsj144.lzc.video.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UploadBroadcast extends BroadcastReceiver {

    private OnUpdateUI onUpdateUI;

    @Override
    public void onReceive(Context context, Intent intent) {
        String progress = intent.getStringExtra("progress");
        onUpdateUI.updateUI(progress);
    }

    public void SetOnUpdateUI(OnUpdateUI onUpdateUI){
        this.onUpdateUI = onUpdateUI;
    }
}
