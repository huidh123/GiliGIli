package com.cch.danmakuproj.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;


import com.cch.danmakuproj.CustomView.BufferingPopupWindow;
import com.cch.danmakuproj.CustomView.VedioPlayGestureControl;
import com.cch.danmakuproj.DanMakuClass.DanMaKuView;
import com.cch.danmakuproj.DanMakuClass.DanMaKuViewConstants;
import com.cch.danmakuproj.Interface.DanMaKuEnigineTimeDriver;
import com.cch.danmakuproj.Interface.VedioControlInterface;
import com.cch.danmakuproj.Interface.VedioPlayStateInterface;
import com.cch.danmakuproj.R;
import com.cch.danmakuproj.Utils.Constant;

import java.lang.ref.WeakReference;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.*;

/**
 * Created by 晨晖 on 2015-05-14.
 */
public class VideoPlayerActivity extends Activity implements DanMaKuEnigineTimeDriver,VedioControlInterface,VedioPlayStateInterface {
    //播放界面控件
    private VideoView vv_video_player;
    private TextView tv_play_time;
    private ImageButton btn_pause;
    private SeekBar sb_player_progress;
    private LinearLayout ll_video_controler;
    private Button btn_switch_layout;
    private BufferingPopupWindow bufferingPopupWindow;
    private DanMaKuView dmkv_danmulayer;
    private LinearLayout ll_danmaku_view;
    private ImageButton btn_start;


    private int Layout_Mode = 0;
    private int Modes[] = new int[]{VideoView.VIDEO_LAYOUT_FIT_PARENT, VideoView.VIDEO_LAYOUT_ORIGIN, VideoView.VIDEO_LAYOUT_SCALE, VideoView.VIDEO_LAYOUT_STRETCH, VideoView.VIDEO_LAYOUT_ZOOM};
    private int videoDuration;
    private int videoCurPlayTime;
    private GestureDetector mGestureDetector;

    //常量
    private final String tag = "VideoPlayerActivity";
    private final static int HIDE_DELAY_TIME = 3000;
    private final static int PROGRESS_UPDATE = 1;
    private final static int HIDE_CONTROLER = 2;

    private Handler handler = new VideoViewHandler(this);

    private void initScreenLayout() {
        //隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //强制横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //界面初始化
        Log.e(tag, "activity is create");
        //manifest中設置參數防止oncreate被調用兩次
        //初始化屏幕参数
        initScreenLayout();

        setContentView(R.layout.activity_video_play_layout);
        Vitamio.initialize(VideoPlayerActivity.this);
        ll_danmaku_view = (LinearLayout) findViewById(R.id.ll_danmaku_view);
        vv_video_player = (VideoView) findViewById(R.id.vv_video_player);
        btn_pause = (ImageButton) findViewById(R.id.btn_pause);
        ll_video_controler = (LinearLayout) findViewById(R.id.ll_video_controler);
        sb_player_progress = (SeekBar) findViewById(R.id.sb_player_progress);
        btn_switch_layout = (Button) findViewById(R.id.btn_switch_layout);
        tv_play_time = (TextView) findViewById(R.id.tv_play_time);
        btn_start = (ImageButton) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("danmu size",DanMaKuViewConstants.DANMAKU_TEXT_SIZE+"");
                DanMaKuViewConstants.DANMAKU_TEXT_SIZE += 5;
            }
        });
        //添加弹幕层到指定层数
        dmkv_danmulayer = new DanMaKuView(VideoPlayerActivity.this);
        ll_danmaku_view.addView(dmkv_danmulayer);
        //添加弹幕时间驱动
        dmkv_danmulayer.setDanMaKuEnigineTimeDriver(this);
        dmkv_danmulayer.setPause(false);
        VedioPlayGestureControl vedioPlayGestureControl = new VedioPlayGestureControl(VideoPlayerActivity.this,this,this);
        mGestureDetector = new GestureDetector(getApplicationContext(), vedioPlayGestureControl);

        sb_player_progress.setOnSeekBarChangeListener(new VideoSeekBarOnSeekListener());
        vv_video_player.setVideoURI(Uri.parse("http://27.221.16.253/edge.v.iask.com/57518558.hlv?KID=sina,viask&Expires=1443024000&ssig=UhQTUCsSal&corp=1"));
        vv_video_player.setTimedTextShown(false);
        vv_video_player.setOnPreparedListener(new VideoPreparedListener());
        vv_video_player.setVideoQuality(View.DRAWING_CACHE_QUALITY_LOW);
        vv_video_player.setOnBufferingUpdateListener(new VideoBufferingListener());
        handler.sendEmptyMessageDelayed(HIDE_CONTROLER, HIDE_DELAY_TIME * 2);
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vv_video_player.isPlaying()) {
                    vv_video_player.pause();
                } else {
                    vv_video_player.start();
                    vv_video_player.requestFocus();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(tag, "video acivity is pause");
        //出现遮挡窗口,停止播放，回收資源
        vv_video_player.pause();
        dmkv_danmulayer.setPause(true);
        vv_video_player.stopPlayback();
        ll_danmaku_view.removeAllViews();
        handler.removeMessages(PROGRESS_UPDATE);
        this.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public long getCurTimeInMills() {
        return vv_video_player.getCurrentPosition();
    }

    @Override
    public void startVideoPlay() {
        vv_video_player.start();
        dmkv_danmulayer.setPause(false);
    }

    @Override
    public void pauseVideoPlay() {
        vv_video_player.pause();
        dmkv_danmulayer.setPause(true);
    }

    @Override
    public void movePlayDuration(int deltaMills) {
        long curPlayDuration = vv_video_player.getDuration();
        vv_video_player.seekTo(curPlayDuration + deltaMills);
    }

    @Override
    public void changeVolume(int deltaVolume) {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        if(currentVolume <= 0){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }else if(currentVolume >= maxVolume){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        }else{
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        }
    }

    @Override
    public void changeBrightness(int deltaCount) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // if (lp.screenBrightness <= 0.1) {
        // return;
        // }
        lp.screenBrightness = lp.screenBrightness + deltaCount / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
        } else if (lp.screenBrightness < 0.2) {
            lp.screenBrightness = (float) 0.2;
        }
        getWindow().setAttributes(lp);
    }

    @Override
    public void isShowControlView(boolean isShow) {
        if(!isShow){
            ll_video_controler.setVisibility(View.INVISIBLE);
            handler.removeMessages(HIDE_CONTROLER);
        }else{
            ll_video_controler.setVisibility(View.VISIBLE);
            handler.sendEmptyMessageDelayed(HIDE_CONTROLER, HIDE_DELAY_TIME);
        }
    }

    @Override
    public void showStatePopWin(PopupWindow window) {
        if(!window.isShowing()){
            Log.e(tag,"顯示彈窗");
            window.showAtLocation(vv_video_player, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public boolean getIsControlViewShown() {
        return ll_video_controler.isShown();
    }

    @Override
    public boolean getIsVideoPlaying() {
        return vv_video_player.isPlaying();
    }

    /**
     * 进度条事件监听器
     */
    class VideoSeekBarOnSeekListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b == true) {
                seekBar.setProgress(i);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            vv_video_player.seekTo(seekBar.getProgress());
            dmkv_danmulayer.setAllplayTime(seekBar.getProgress());
        }
    }

    class VideoPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.pause();
            videoDuration = (int) mp.getDuration();
            Log.e(tag, "初始化完成，开始缓冲");
            handler.sendEmptyMessage(PROGRESS_UPDATE);
        }
    }

    class VideoBufferingListener implements MediaPlayer.OnBufferingUpdateListener {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (percent < 95) {
                // Log.e(tag, "正在缓冲" + percent);
                if (bufferingPopupWindow == null) {
                    bufferingPopupWindow = new BufferingPopupWindow(VideoPlayerActivity.this);
                } else if (!bufferingPopupWindow.isShowing() || mp.isPlaying()) {
                    bufferingPopupWindow.showAtLocation(vv_video_player, Gravity.CENTER, 0, 0);
                    pauseVideoPlay();
                } else if (bufferingPopupWindow.isShowing()) {
                    bufferingPopupWindow.setProgressPercent(percent);
                }
            } else if (percent >= 95) {
                //Log.e(tag, "缓冲完成");
                bufferingPopupWindow.dismiss();
                startVideoPlay();
            }
        }
    }

    /**
     * 手势控制类，控制对于主界面的手势操作
     */
    class VideoActivityGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (vv_video_player.isPlaying()) {
                pauseVideoPlay();
            } else {
                startVideoPlay();
                vv_video_player.requestFocus();
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (ll_video_controler.isShown()) {
                ll_video_controler.setVisibility(View.INVISIBLE);
                handler.removeMessages(HIDE_CONTROLER);
            } else {
                ll_video_controler.setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(HIDE_CONTROLER, HIDE_DELAY_TIME);
            }
            return super.onSingleTapUp(e);
        }
    }

    static class VideoViewHandler extends Handler{

        private final WeakReference<VideoPlayerActivity> activityWeakReference;

        public VideoViewHandler(VideoPlayerActivity activity){
            activityWeakReference = new WeakReference<VideoPlayerActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VideoPlayerActivity theClass = activityWeakReference.get();
            if(theClass == null){
                return ;
            }
            switch (msg.what) {
                case PROGRESS_UPDATE:
                    theClass.sb_player_progress.setMax((int) theClass.vv_video_player.getDuration());
                    theClass.sb_player_progress.setProgress((int) theClass.vv_video_player.getCurrentPosition());
                    theClass.tv_play_time.setText(Constant.changeMSTimeToStr((int) theClass.vv_video_player.getCurrentPosition()) + "/" + Constant.changeMSTimeToStr(theClass.vv_video_player.getDuration()));
                    this.sendEmptyMessage(PROGRESS_UPDATE);
                    break;
                case HIDE_CONTROLER:
                    theClass.ll_video_controler.setVisibility(View.INVISIBLE);
                    break;
                }
            }
    }
}
