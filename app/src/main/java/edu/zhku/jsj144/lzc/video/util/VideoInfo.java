package edu.zhku.jsj144.lzc.video.util;

public class VideoInfo {

    private String thumbPath;
    private String path;
    private String title;
    private String dateTaken;
    private String mimeType;
    private String duration;

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "thumbPath='" + thumbPath + '\'' +
                ", path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", dateTaken='" + dateTaken + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
