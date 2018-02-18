package edu.zhku.jsj144.lzc.video.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.util.StatusBar;

public class DiscoverPageFragment extends Fragment {

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_page_discover, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.discoverToolbar);
        toolbar.setPadding(0, StatusBar.getStatusHeight(activity), 0, 0);

        return rootView;
    }
}
