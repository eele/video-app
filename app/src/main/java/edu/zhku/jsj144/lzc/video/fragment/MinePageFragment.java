package edu.zhku.jsj144.lzc.video.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.activity.UploadChoiceActivity;

public class MinePageFragment extends Fragment {

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_page_mine, container, false);

        Button uploadButton = (Button) rootView.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        return rootView;
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(activity, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_upload, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_upload_new:

                        return true;
                    case R.id.action_upload_cho:
                        Intent intent = new Intent(activity, UploadChoiceActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

}
