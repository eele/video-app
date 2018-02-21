package edu.zhku.jsj144.lzc.video.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class VideoInfoUtil {

    public static List<VideoInfo> getVideoList(Context context) {
        List<VideoInfo> sysVideoList = new ArrayList<VideoInfo>(); // 视频信息集合
        String[] thumbColumns = {
                MediaStore.Video.Thumbnails.DATA,  // 视频缩略图的文件路径
                MediaStore.Video.Thumbnails.VIDEO_ID
        };

        String[] mediaColumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.DURATION
        };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, MediaStore.Video.Media.DATE_MODIFIED);

        if(cursor==null){
            return sysVideoList;
        }
        if (cursor.moveToFirst()) {
            do {
                VideoInfo info = new VideoInfo();
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor = context.getContentResolver().query(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    info.setThumbPath(thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }
                info.setPath(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                info.setTitle(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                info.setDateTaken(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)));
                info.setMimeType(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
                info.setDuration(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                sysVideoList.add(info);
            } while (cursor.moveToNext());
        }

        return sysVideoList;
    }
}