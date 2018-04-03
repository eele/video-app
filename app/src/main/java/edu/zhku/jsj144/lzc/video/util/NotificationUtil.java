package edu.zhku.jsj144.lzc.video.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import edu.zhku.jsj144.lzc.video.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtil {

    private static NotificationManager mNotificationManager;

    public static void showUploadNotifiction(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("Video")//设置通知栏标题
                .setContentText("一个视频正在上传...") //设置通知栏显示内容
//                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                .setNumber(1) //设置通知集合的数量
                .setTicker("一个视频正在上传...") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setOngoing(true) //true，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setProgress(0, 0, true)
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        mNotificationManager.notify(0, mBuilder.build());
    }

    public static void cancelUploadNotifiction() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(0);
        }
    }
}
