package com.cch.danmakuproj.DanMakuClass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.cch.danmakuproj.Interface.DanMaKuEnigineTimeDriver;
import com.cch.danmakuproj.Interface.Observer;
import com.cch.danmakuproj.Interface.Publisher;
import com.cch.danmakuproj.Utils.Constants;
import com.cch.danmakuproj.javaBean.DanMaKu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 晨晖 on 2015-04-11.
 */
public class DanMaKuView extends SurfaceView implements SurfaceHolder.Callback, Publisher {
    private boolean isFreshThreadRun = false;
    private SurfaceHolder surfaceHolder = null;
    private Thread freshTh = null;
    private DanMaKuManeger danMuManager;
    private int viewHeight = 0;

    private int viewWitdh = 0;
    private int maxColumes = 7;
    private long allplayTime = 0;
    private static int playCompleteDanmakuCount = 0;

    private DanMaKuEnigineTimeDriver danMaKuEnigineTimeDriver = null;
    private int sreenHeight;
    private int screenWidth;
    private ArrayList<DanMaKu> playDanMaKuList;
    private ArrayList<Observer> observers;
    public Handler handler;
    private Context context;
    public boolean isFPSshow = true;

    public static final String tag = "DanMaKuVIew";
    private int MAX_FPS_DRAWTIMES_LIMIT = 50;
    private double MILLS_ONE_SECOND = 1000;
    //帧数计算使用的数据结构，保存一定数量的没帧绘图时间
    private List<Long> drawUseMills;
    private long lastDrawTime = 0;
    private Rect screenRect;
    private boolean isPause = true;
    /**
     * 保存弹幕播放器的状态信息
     */
    private DanmakuViewDrawStatus danmakuViewDrawStatus;

    static class MyHandler extends Handler {

        private WeakReference<DanMaKuView> danMaKuViewWeakReference;

        public MyHandler(DanMaKuView danMaKuView) {
            danMaKuViewWeakReference = new WeakReference<DanMaKuView>(danMaKuView);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DanMaKuView danMaKuView = danMaKuViewWeakReference.get();
            if (msg.what == 1) {
                danMaKuView.notifyAllObserver();
            }
        }

    }

    public DanMaKuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initSurfaceView();
    }

    public DanMaKuView(Context context) {
        super(context);
        this.context = context;
        initSurfaceView();
    }

    /**
     * 设置DanMaKUView播放的弹幕源
     *
     * @param playDanMaKuList
     */
    public void setPlayDanMaKuList(ArrayList<DanMaKu> playDanMaKuList) {
        this.playDanMaKuList = playDanMaKuList;
        Collections.sort(playDanMaKuList, new SortByTimeMills());
    }

    public void stopDanMakuView() {
        isFreshThreadRun = false;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    public long getAllplayTime() {
        return allplayTime;
    }

    /**
     * 设置弹幕引擎当前播放时间，在此方法中会定位到当前应当播放弹幕条数
     *
     * @param allplayTime
     */
    public void setAllplayTime(int allplayTime) {
        this.allplayTime = allplayTime;
        //定位到当前应当播放弹幕条数，注意：1500为位移量，将屏幕清空
        playCompleteDanmakuCount = seekToDanMaKuCompletePlayedCount(allplayTime + 1500);
        //清除當前彈幕緩存，不再添加新的彈幕
        danMuManager.clearDanmuCache();
    }

    /**
     * 查询当前播放时间mills应当播放的弹幕序号
     *
     * @param playTime
     * @return
     */
    private int seekToDanMaKuCompletePlayedCount(int playTime) {
        int count = 0;
        for (int i = 0; i < playDanMaKuList.size(); i++) {
            if (playTime >= playDanMaKuList.get(i).getShowTime()) {
                count = i;
            }
        }
        return count;
    }

    /**
     * 初始化View
     */
    private void initSurfaceView() {
        danMuManager = new DanMaKuManeger();
        surfaceHolder = getHolder();
        this.setZOrderMediaOverlay(true);//设置画布  背景透明
        this.setWillNotCacheDrawing(true);
        this.setDrawingCacheEnabled(false);
        this.setWillNotDraw(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder.addCallback(this);
        //freshTh.setName("弹幕绘图刷新进程");
        handler = new MyHandler(this);

        observers = new ArrayList<Observer>();

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        sreenHeight = display.getHeight();
        screenWidth = display.getWidth();
        screenRect = new Rect(0, 0, screenWidth, sreenHeight);
        drawUseMills = new ArrayList<Long>();
        Log.e(tag, "弹幕播放器高度=" + sreenHeight + "宽度=" + screenWidth);
        setPlayDanMaKuList(Constants.danmuList);
        this.setLayoutParams(new ViewGroup.LayoutParams(screenWidth, sreenHeight));
        danmakuViewDrawStatus = new DanmakuViewDrawStatus();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(tag, "surfaceCreated");
        doDraw();
        freshTh = new Thread(new OnFreshCanvasRunable());
        isFreshThreadRun = true;
        freshTh.setName("彈幕刷新線程");
        freshTh.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(tag, "弹幕界面改变");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(tag, "弹幕界面被销毁");
        isFreshThreadRun = false;
        try {
            freshTh.join();
            Log.e(tag, "停止线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = getMeasuredHeight();
        viewWitdh = getMeasuredWidth();
        Log.e(tag, "弹幕播放器测量高度=" + viewHeight + "宽度=" + viewWitdh);
        setMeasuredDimension(viewWitdh, viewHeight);
        danMuManager.initDanMuManagerConfig(getContext(), viewHeight, viewWitdh, maxColumes);
    }

    private long doDraw() {
        Canvas canvas = null;
        long startTime = System.currentTimeMillis();
        canvas = surfaceHolder.lockCanvas(screenRect);
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            for (DanMakuSprite item : danMuManager.showingDanmuSpList) {
                item.draw(canvas);
            }
            if (isFPSshow) {
                drawFPS(canvas);
                lastDrawTime = System.currentTimeMillis();
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private void drawFPS(Canvas canvas) {
        drawUseMills.add(lastDrawTime);
        if (drawUseMills.size() > MAX_FPS_DRAWTIMES_LIMIT) {
            drawUseMills.remove(0);
        }
        double tempFPS = MILLS_ONE_SECOND / (lastDrawTime - drawUseMills.get(0)) * drawUseMills.size();
        canvas.drawText(String.format("当前FPS:%1$.2f", tempFPS), 10, 400, PaintManeger.getInstance().getPaintByColor("#FF0000"));
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObserver() {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update();
        }
    }

    /**
     * 设置弹幕时间驱动，用于View与时间驱动进行同步
     * @param danMaKuEnigineTimeDriver
     */
    public void setDanMaKuEnigineTimeDriver(DanMaKuEnigineTimeDriver danMaKuEnigineTimeDriver) {
        this.danMaKuEnigineTimeDriver = danMaKuEnigineTimeDriver;
    }
    public void removeDanMaKuEnigineTimeDriver() {
        this.danMaKuEnigineTimeDriver = null;
    }


    /**
     * 用于弹幕引擎与时间驱动同步方法
     * @return 0 相差时间在误差范围之内 ， 无需同步
     *          >0 弹幕播放器时间快于时间驱动
     *          <0 弹幕播放器事件慢于时间驱动
     */
    private long synchronousWithTimeDriver(){
        long driveTime;
        if(danMaKuEnigineTimeDriver == null){
            driveTime = allplayTime;
        }else{
            driveTime = danMaKuEnigineTimeDriver.getCurTimeInMills();
        }
        if(Math.abs(allplayTime - driveTime) < 400){
            return 0;
        }
        return (allplayTime - driveTime);
    }
    /**
     * 用于弹幕时间排序的类
     */
    class SortByTimeMills implements Comparator<DanMaKu> {

        @Override
        public int compare(DanMaKu lhs, DanMaKu rhs) {
            if (lhs.getShowTime() < rhs.getShowTime()) {
                return -1;
            } else if (lhs.getShowTime() > rhs.getShowTime()) {
                return 1;
            }
            return 0;
        }
    }

    class DanmakuViewDrawStatus {
        public long totalDrawFrameTime;
        public long drawUseTime;
        public long caculateUseTime;
        public DanMaKuManeger.DanMakusStatus danMakuManagerStatus;
    }

    /**
     * surface刷新线程
     */
    class OnFreshCanvasRunable implements Runnable {

        long threadCricleStartTime = 0;
        long threadOverTime = 0;

        @Override
        public void run() {
            //初始化两个时间
            threadCricleStartTime = System.currentTimeMillis();
            threadOverTime = System.currentTimeMillis();
            while (isFreshThreadRun) {
                try {
                    synchronized (surfaceHolder) {
                        handler.sendEmptyMessage(1);
                        threadCricleStartTime = System.currentTimeMillis();
                        long freshTime = threadCricleStartTime - threadOverTime;
                        threadOverTime = threadCricleStartTime;
                        //当非暂停时，移动弹幕位置，暂停时不移动位置
                        if (!isPause) {
                            danmakuViewDrawStatus.danMakuManagerStatus = danMuManager.freshAllShowingDanMu(allplayTime);
                            danmakuViewDrawStatus.caculateUseTime = danmakuViewDrawStatus.danMakuManagerStatus.moveDanmakusUseTimeMills;
                            //同步时间驱动
                            long deltaTime = synchronousWithTimeDriver();
                            if(deltaTime == 0){
                                allplayTime += freshTime;
                                while (playDanMaKuList.size() > playCompleteDanmakuCount && playDanMaKuList.get(playCompleteDanmakuCount).getShowTime() <= allplayTime) {
                                    danMuManager.addDanMu(playDanMaKuList.get(playCompleteDanmakuCount));
                                    playCompleteDanmakuCount++;
                                }
                            }else if(deltaTime > 0){
                                Log.e(tag,"时间驱动与引擎事件差距过大，暂停弹幕引擎");
                            }else{
                                allplayTime += freshTime;
                            }
                        }

                        //绘制弹幕
                        long drawTimeMills = doDraw();
                        danmakuViewDrawStatus.drawUseTime = drawTimeMills;
                        danmakuViewDrawStatus.totalDrawFrameTime = danmakuViewDrawStatus.drawUseTime + danmakuViewDrawStatus.caculateUseTime;
                        Log.e(tag, String.format("当前播放时间：%s , 弹幕引擎播放时间：%s", danMaKuEnigineTimeDriver.getCurTimeInMills(), allplayTime));
                        Log.e(tag, String.format("绘画用时：%s , 计算用时：%s", danmakuViewDrawStatus.drawUseTime,danmakuViewDrawStatus.caculateUseTime));
                        Log.e(tag, String.format("屏幕中弹幕：%s , 等待弹幕：%s ", danmakuViewDrawStatus.danMakuManagerStatus.showingDanmakuCount, danmakuViewDrawStatus.danMakuManagerStatus.waitingDanmakuCount));
                        Thread.currentThread().sleep(Math.max(0, (int) (16 - danmakuViewDrawStatus.totalDrawFrameTime)));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
