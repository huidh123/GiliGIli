package com.cch.danmakuproj.CustomView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import com.cch.danmakuproj.javaBean.DanMuItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 晨晖 on 2015-04-11.
 */
public class DanMaKuView extends SurfaceView implements SurfaceHolder.Callback, Publisher {
    private boolean isFreshThreadRun = false;
    private SurfaceHolder surfaceHolder = null;
    private Thread freshTh = null;
    private Paint danmuPaint = null;
    private DanMuManager danMuManager;
    public static final String tag = "DanMaKuVIew";
    private int viewHeight = 0;
    private int viewWitdh = 0;
    private int maxColumes = 13;
    private int allplayTime = 0;
    /**
     * 弹幕描边宽度
     */
    private int danmuStrokeWitdh = 2;
    /**
     *
     */
    private String danmuStrokeColor = "#000000";
    /**
     * 弹幕字体大小，默认30
     */
    private int danmuTextSize = 35;

    private static int playCompleteDanmakuCount = 0;
    private DanMaKuEnigineTimeDriver danMaKuEnigineTimeDriver = null;
    private int sreenHeight;
    private int screenWidth;
    private ArrayList<DanMaKu> playDanMaKuList;

    private String[] paintNameArr = null;
    private Paint[] paintArr = null;

    Constants constants;
    private ArrayList<Observer> observers;

    public Handler handler;

    static class MyHandler extends Handler {
        private WeakReference<DanMaKuView> danMaKuViewWeakReference;
        public MyHandler(DanMaKuView danMaKuView){
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
        Log.e(tag, "construct");
        initSurfaceView();
        observers = new ArrayList<Observer>();
    }

    public DanMaKuView(Context context) {
        super(context);
        initSurfaceView();
        observers = new ArrayList<Observer>();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        sreenHeight = display.getHeight();
        screenWidth = display.getWidth();
        Log.e(tag, "弹幕播放器高度=" + sreenHeight + "宽度=" + screenWidth);
        setPlayDanMaKuList(Constants.danmuList);
        this.setLayoutParams(new ViewGroup.LayoutParams(screenWidth, sreenHeight));
    }

    /**
     * 设置DanMaKUView播放的弹幕源
     *
     * @param playDanMaKuList
     */
    public void setPlayDanMaKuList(ArrayList<DanMaKu> playDanMaKuList) {
        this.playDanMaKuList = playDanMaKuList;
        Map<String, Paint> paintPool = new HashMap<String, Paint>();
        //初始化画比缓冲池
        for (DanMaKu danMaKu : playDanMaKuList) {

            if (paintPool.containsKey(danMaKu.getDanmuColor())) {
            } else {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setTextSize(danmuTextSize);
                paint.setColor(Color.parseColor(danMaKu.getDanmuColor()));
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                paint.setDither(true);
                paint.setShadowLayer(danmuStrokeWitdh, 0, 0, Color.BLACK);
                paintPool.put(danMaKu.getDanmuColor(), paint);
            }
        }

        paintNameArr = new String[paintPool.size()];
        paintArr = new Paint[paintPool.size()];

        int i = 0;
        for (Map.Entry<String, Paint> entry : paintPool.entrySet()) {
            paintNameArr[i] = entry.getKey();
            paintArr[i] = entry.getValue();
            i++;
        }
    }

    public void stopDanMakuView(){
        isFreshThreadRun = false;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    private boolean isPause = true;

    public int getAllplayTime() {
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

        danmuPaint = new Paint();
        danmuPaint.setColor(Color.BLACK);
        danmuPaint.setTextSize(30);
        danmuPaint.setAntiAlias(true);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        danmuPaint.setTypeface(font);

        danMuManager = new DanMuManager();
        surfaceHolder = getHolder();
        this.setZOrderMediaOverlay(true);//设置画布  背景透明
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        surfaceHolder.addCallback(this);

        //freshTh.setName("弹幕绘图刷新进程");
        handler = new MyHandler(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(tag, "surfaceCreated");
        Canvas canvas = holder.lockCanvas();
        doDraw(canvas);
        holder.unlockCanvasAndPost(canvas);
        freshTh = new Thread(new OnFreshCanvasRunable());
        isFreshThreadRun = true;
        freshTh.setName("彈幕刷新線程");
        freshTh.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(tag,"弹幕界面改变");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(tag,"弹幕界面被销毁");
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
        danMuManager.initDanMuManagerConfig(getContext(), viewHeight, viewWitdh, maxColumes, danmuTextSize);

    }

    private void doDraw(Canvas canvas) {
        //Log.e(tag,"ondraw");
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            for (DanMuItem item : danMuManager.showingDanMuList) {
                Paint paint = findPaintInPool(item.danmuColor);
                if (paint == null) {
                    Log.e(tag, "弹幕颜色" + item.danmuColor + "::画笔池num" + paintArr.length);
                } else {
                    canvas.drawText(item.message, item.curX, item.curY, paint);
                }

            }
        }
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

    public void setDanMaKuEnigineTimeDriver(DanMaKuEnigineTimeDriver danMaKuEnigineTimeDriver) {
        this.danMaKuEnigineTimeDriver = danMaKuEnigineTimeDriver;
    }

    /**
     * 查询Map中是否存在key为paintColor的画笔，若不存在返回空
     *
     * @param paintColor 查询画笔名称
     * @return 画笔对象，若不存在返回null
     */
    private Paint findPaintInPool(String paintColor) {
        for (int i = 0; i < paintNameArr.length; i++) {
            if (paintNameArr[i].equals(paintColor)) {
                return paintArr[i];
            }
        }
        return null;
    }


    public void removeDanMaKuEnigineTimeDriver() {
        this.danMaKuEnigineTimeDriver = null;
    }

    /**
     * surface刷新线程
     */
    class OnFreshCanvasRunable implements Runnable{

        long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            Canvas canvas = null;
            long curTime = 0;
            while (isFreshThreadRun) {
                try {
                    synchronized (surfaceHolder) {
                        handler.sendEmptyMessage(1);
                        curTime = System.currentTimeMillis();
                        canvas = surfaceHolder.lockCanvas(null);
                        //Log.e(tag, "线程运行" + isPause);
                        //Log.e(tag,"刷新时间"+(System.currentTimeMillis() - startTime)+"ms");
                        //Log.e(tag, "当前运行时间：" + allplayTime);
                        long freshTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        //Log.d("FreshTime","刷新用时"+freshTime+"ms");
                        //当非暂停时，移动弹幕位置，暂停时不移动位置
                        if (!isPause) {
                            danMuManager.freshAllShowingDanMu(allplayTime);
                            if (danMaKuEnigineTimeDriver == null) {
                                allplayTime += freshTime;
                            } else {
                                allplayTime = (int) danMaKuEnigineTimeDriver.getCurTimeInMills();
                                //Log.e(tag,allplayTime+"");
                            }
                            while (playDanMaKuList.size() > playCompleteDanmakuCount && playDanMaKuList.get(playCompleteDanmakuCount).getShowTime() <= allplayTime) {
                                danMuManager.addDanMu(playDanMaKuList.get(playCompleteDanmakuCount));
                                playCompleteDanmakuCount++;
                            }
                        }
                        doDraw(canvas);
                        long deltaTime = System.currentTimeMillis() - curTime;
                        Thread.currentThread().sleep(Math.max(0,(int)(33 - deltaTime)));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
