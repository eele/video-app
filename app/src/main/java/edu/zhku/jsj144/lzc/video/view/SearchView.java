package edu.zhku.jsj144.lzc.video.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import edu.zhku.jsj144.lzc.video.R;

public class SearchView extends android.support.v7.widget.SearchView {
    public SearchView(Context context) {
        super(context);
        this.setBackgroundResource(R.drawable.search_box_border);
        this.setQueryHint("搜搜看");
        this.setIconified(false);//设置searchView处于展开状态
        this.onActionViewExpanded();// 当展开无输入内容的时候，没有关闭的图标

    }
}
