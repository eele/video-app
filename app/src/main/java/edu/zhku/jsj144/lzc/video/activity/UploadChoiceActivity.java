package edu.zhku.jsj144.lzc.video.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.zhku.jsj144.lzc.video.R;
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
        toolbar.inflateMenu(R.menu.upload_choice_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.upload_choice_close) {
                    UploadChoiceActivity.this.finish();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<VideoInfo> videoList = VideoInfoUtil.getVideoList(UploadChoiceActivity.this);
        ViewGroup group = (ViewGroup) findViewById(R.id.imageViewGroup);
        ImageView imageView = new ImageView(this);  //创建imageview
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width / 3 - 6, width / 4 - 6));  //image的布局方式
        imageView.setImageURI(Uri.fromFile(new File(videoList.get(0).getThumbPath())));

        TextView dateTextView = new TextView(this);
        dateTextView.setText("2001-01-01");
        dateTextView.setHeight(50);
        dateTextView.setWidth(260);
        dateTextView.setPadding(0,0,0,0);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        dateTextView.setGravity(Gravity.CENTER_HORIZONTAL);
//        dateTextView.setLayoutParams(layoutParams);
        dateTextView.setTextColor(Color.WHITE);
        dateTextView.getPaint().setFakeBoldText(true);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(imageView);
        frameLayout.addView(dateTextView);

        group.addView(frameLayout);  //添加到布局容器中，显示图片。
    }
}
