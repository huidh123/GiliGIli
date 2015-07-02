package com.cch.danmakuproj.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cch.danmakuproj.CustomView.DanMaKuView;
import com.cch.danmakuproj.Interface.Observer;
import com.cch.danmakuproj.R;
import com.cch.danmakuproj.Utils.Constants;


public class MainActivity2Activity extends ActionBarActivity implements Observer {

    private DanMaKuView danMaKuView;
    private TextView tv_play_time;
    private Button btn_pause;
    private Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        danMaKuView = (DanMaKuView) findViewById(R.id.danmuView);
        danMaKuView.addObserver(this);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        tv_play_time = (TextView) findViewById(R.id.tv_play_time);
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(danMaKuView.isPause()){
                    danMaKuView.setPause(false);
                }else{
                    danMaKuView.setPause(true);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update() {
        tv_play_time.setText(danMaKuView.getAllplayTime() + "/" + Constants.vedioTimeLength*1000);
    }
}
