package edu.zhku.jsj144.lzc.video.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.util.UnitUtil;
import edu.zhku.jsj144.lzc.video.util.VideoInfo;
import edu.zhku.jsj144.lzc.video.util.VideoInfoUtil;

import java.io.File;
import java.util.List;

public class UploadChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_choice);

        Toolbar toolbar = (Toolbar) findViewById(R.id.uploadToolbar);
        toolbar.inflateMenu(R.menu.file);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadChoiceActivity.this.finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new MaterialFilePicker()
                        .withActivity(UploadChoiceActivity.this)
                        .withTitle("请选择")
                        .withRootPath(Environment.getExternalStorageDirectory().getAbsolutePath())
                        .withCloseMenu(true)
                        .withRequestCode(123)
                        .withFilterDirectories(true)
                        .start();
                return true;
            }
        });

        List<VideoInfo> videoList = VideoInfoUtil.getVideoList(UploadChoiceActivity.this);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.videoViewGroup);
        String dateTaken = "";
        for (VideoInfo videoInfo : videoList) {
            String newDateTaken = VideoInfoUtil.stampToDate(videoInfo.getDateTaken());
            if (! newDateTaken.equals(dateTaken)) {  // 显示拍摄日期
                dateTaken = newDateTaken;
                addTextView(gridLayout, dateTaken);
            }

            // 显示缩略图
            addVideoView(gridLayout, videoInfo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK ){
            if (requestCode == 123) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                Intent intent = new Intent(UploadChoiceActivity.this, UploadPreviewActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("title",
                        filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".")));
                startActivityForResult(intent, 1);
            } else {
                finish();
            }
        }
    }

    /**
     * 显示拍摄日期
     * @param gridLayout
     * @param dateTaken
     */
    private void addTextView(GridLayout gridLayout, String dateTaken) {
        TextView dateTextView = new TextView(this);
        dateTextView.setText(dateTaken);
        dateTextView.getPaint().setFakeBoldText(true);
        dateTextView.setTextColor(Color.GRAY);
        dateTextView.setBackgroundColor(Color.WHITE);
        dateTextView.setTextSize(16f);
        dateTextView.setPadding(UnitUtil.dip2px(this, 20),0,0,0);
        dateTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT | Gravity.FILL_HORIZONTAL);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 3,3f);
        layoutParams.height = UnitUtil.dip2px(this, 40);
        layoutParams.topMargin = UnitUtil.dip2px(this, 3);

        gridLayout.addView(dateTextView, layoutParams);
    }

    /**
     * 显示视频缩略图
     * @param gridLayout
     * @param videoInfo
     */
    private void addVideoView(GridLayout gridLayout, final VideoInfo videoInfo) {
        ImageView imageView = new ImageView(this);
        if (videoInfo.getThumbPath() != null) {
            imageView.setImageURI(Uri.fromFile(new File(videoInfo.getThumbPath())));
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.mipmap.ic_video_default);
        }
        imageView.setBackgroundColor(Color.BLACK);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED,1f);
        layoutParams.height = UnitUtil.dip2px(this, 40);
        layoutParams.leftMargin = UnitUtil.dip2px(this, 3);
        layoutParams.topMargin = UnitUtil.dip2px(this, 3);
        layoutParams.height = UnitUtil.dip2px(this, 90);
        imageView.setLayoutParams(layoutParams);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadChoiceActivity.this, UploadPreviewActivity.class);
                intent.putExtra("path", videoInfo.getPath());
                intent.putExtra("title", videoInfo.getTitle());
                startActivityForResult(intent, 1);
            }
        });

        gridLayout.addView(imageView);
    }

}
