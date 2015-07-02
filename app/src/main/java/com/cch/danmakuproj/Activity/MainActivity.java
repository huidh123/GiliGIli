package com.cch.danmakuproj.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.cch.danmakuproj.CustomView.BufferingPopupWindow;
import com.cch.danmakuproj.R;
import com.cch.danmakuproj.Utils.Constants;


public class MainActivity extends Activity {

    private Button button;
    private Button btn_play_vedio;
    private Button btn_pop_win;
    private String tag = "Activity_statu_test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.e(tag, "activity oncreate");
//        Log.e(tag, "activity 开始");

        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        btn_play_vedio = (Button) findViewById(R.id.btn_play_video);
        btn_pop_win = (Button) findViewById(R.id.btn_pop_win);
        btn_pop_win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BufferingPopupWindow bufferingPopupWindow = new BufferingPopupWindow(getApplicationContext());
                bufferingPopupWindow.setFocusable(true);
                bufferingPopupWindow.setOutsideTouchable(true);
                bufferingPopupWindow.showAtLocation(btn_play_vedio, Gravity.BOTTOM,20,20);
            }
        });
        btn_play_vedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VideoPlayerActivity.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity2Activity.class));
            }
        });
        Constants constants = new Constants();
    }
}
