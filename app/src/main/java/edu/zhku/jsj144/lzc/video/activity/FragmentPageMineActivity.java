package edu.zhku.jsj144.lzc.video.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import edu.zhku.jsj144.lzc.video.R;

public class FragmentPageMineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page_mine);

        Button uploadButton = (Button) findViewById(R.id.upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("aa", "点击事件");
            }
        });
    }
}
