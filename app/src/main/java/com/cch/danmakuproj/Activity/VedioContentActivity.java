package com.cch.danmakuproj.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.cch.danmakuproj.AndroidUtils.L;
import com.cch.danmakuproj.DanMakuClass.DanMaKuViewConstants;
import com.cch.danmakuproj.R;
import com.cch.danmakuproj.Utils.BiliDanmakuXMLParser;
import com.cch.danmakuproj.Utils.Constant;
import com.cch.danmakuproj.Utils.Constants;
import com.cch.danmakuproj.Utils.DataUtils;
import com.cch.danmakuproj.Utils.NetWorkUtils;
import com.cch.danmakuproj.javaBean.DanMaKu;
import com.cch.danmakuproj.javaBean.DanmuFileData;

import org.apache.commons.io.output.ClosedOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by 晨晖 on 2015-06-28.
 */
public class VedioContentActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private Button btn_start_play;
    private WebView webView;
    private TextView tv_vedio_detail;
    private String tag = "VedioContentActivity";
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_content_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_start_play = (Button) findViewById(R.id.btn_start_play);
        tv_vedio_detail = (TextView) findViewById(R.id.tv_vedio_detail);

        loadingDialog = new ProgressDialog(VedioContentActivity.this);
        loadingDialog.setMessage("正在加载弹幕文件");
        loadingDialog.setCancelable(false);
        setToolbar();
        btn_start_play.setOnClickListener(new OnPlayBtnClickListener());
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class OnPlayBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            NetGetDanmakuFileTask netGetDanmakuFileTask = new NetGetDanmakuFileTask(4421484);
            netGetDanmakuFileTask.execute();
        }
    }

    /**
     * 获取弹幕文件异步任务
     */
    class NetGetDanmakuFileTask extends AsyncTask<Void, Void, DanmuFileData> {
        private int chatId;

        private String resStr;

        public NetGetDanmakuFileTask(int chatId) {
            this.chatId = chatId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show();
        }

        @Override
        protected DanmuFileData doInBackground(Void... voids) {
            Boolean isSuccessed = false;

            try {
                isSuccessed = NetWorkUtils.downLoadDanmuFile(chatId);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return null;
            }
            L.e("网络请求成功" + isSuccessed);
            if (!isSuccessed) {
                return null;
            }

            File danmuFile = new File(Constant.DANMU_FILE_SAVE_PATH + chatId);
            if (!danmuFile.exists()) {
                return null;
            }
            L.e("文件解析成功");
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(danmuFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                L.e("文件流读取失败");
                return null;
            }
            L.e("文件流读取成功");
            BiliDanmakuXMLParser biliDanmakuXMLParser = (BiliDanmakuXMLParser) DataUtils.parseXMLToDanmakuFile(fileInputStream, new BiliDanmakuXMLParser());
            return biliDanmakuXMLParser.getDanmakuFileData();
        }

        @Override
        protected void onPostExecute(DanmuFileData danmuFileData) {
            super.onPostExecute(danmuFileData);
            loadingDialog.dismiss();
            if (danmuFileData == null) {
                Log.e(tag, "弹幕解析失败");
            } else {
                Log.e(tag, "弹幕服务器" + danmuFileData.getChatServer());
                Log.e(tag, "弹幕数量" + danmuFileData.getDanmuList().size());
                Collections.sort(danmuFileData.getDanmuList(), new SortByTimeDesc());
                //设置弹幕到全局变量处，准备播放
                Constants.danmuList = danmuFileData.getDanmuList();
                startActivity(new Intent(VedioContentActivity.this, VideoPlayerActivity.class));
            }
        }
    }

    class SortByTimeDesc implements Comparator<DanMaKu> {

        @Override
        public int compare(DanMaKu danMaKu, DanMaKu t1) {

            return danMaKu.getShowTime() < t1.getShowTime() ? -1 : 1;
        }
    }

    public ArrayList getTestList(){
        ArrayList<DanMaKu> danMakus = new ArrayList<>();
        danMakus.add(new DanMaKu("测试111111111111111111",1000, DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT,"#000000"));
        danMakus.add(new DanMaKu("测试222222222222222222",1000, DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT,"#000000"));
        danMakus.add(new DanMaKu("测试3333333333333333331",1000, DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT,"#000000"));
        danMakus.add(new DanMaKu("测试444444444444444444",1000, DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT,"#000000"));
        danMakus.add(new DanMaKu("测试55555555555555555",1000, DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT,"#000000"));
        danMakus.add(new DanMaKu("测试16666666666666666",1000, DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT,"#000000"));
        danMakus.add(new DanMaKu("测试777777777777777711",1000, DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT,"#000000"));
        return danMakus;
    }
}
