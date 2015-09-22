package com.cch.danmakuproj.DanMakuClass;

import android.content.Context;
import android.util.Log;

import com.cch.danmakuproj.javaBean.DanMaKu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by 晨晖 on 2015-07-31.
 */
public class DanMaKuManeger {
    public static final String tag = "DanMuManager";
    /**
     * 正在显示的弹幕列表
     */
    public ArrayList<DanMakuSprite> showingDanmuSpList = new ArrayList<DanMakuSprite>();
    private ArrayList<DanMaKu> waitingDanMuList = new ArrayList<DanMaKu>();
    private HashSet<DanMakuSprite> needToRemoveSpList = new HashSet<DanMakuSprite>();
    private HashSet<DanMakuSprite> needToAddSpList = new HashSet<DanMakuSprite>();

    public int maxColumes = 0;
    private ChannelManeger channelManeger;
    private BitmapManager bitmapManager;
    public Context context;

    public DanMaKuManeger() {

    }

    public void initDanMuManagerConfig(Context context, int viewHeight, int viewWidth, int maxColumes) {
        DanMaKuViewConstants.viewHeight = viewHeight;
        DanMaKuViewConstants.viewWidth = viewWidth;
        this.maxColumes = maxColumes;
        this.context = context;
        channelManeger = new ChannelManeger(maxColumes);
        bitmapManager = BitmapManager.getInstance();
    }

    /**
     * 更新弹幕位置方法，在每一帧中调用
     */
    public DanMakusStatus freshAllShowingDanMu(long curMills) {
        //初始化本次弹幕逻辑计算的方法调用状态参数
        DanMakusStatus danMakusStatus = new DanMakusStatus();
        long methodStartTime = System.currentTimeMillis();

        channelManeger.clearAllChannelUser();
        int showingDanmuCount = showingDanmuSpList.size();
        for (int i = 0; i < showingDanmuCount; i++) {
            DanMakuSprite tempDanMakuSprite = showingDanmuSpList.get(i);
            //获取弹幕与屏幕位置关系
            DanMaKuViewConstants.DanmuState curDanmuState = tempDanMakuSprite.getDanMaKuStatus();
            //移除离开屏幕的弹幕
            if (curDanmuState == DanMaKuViewConstants.DanmuState.en_outOfScreen) {
                needToRemoveSpList.add(tempDanMakuSprite);
            } else if (curDanmuState == DanMaKuViewConstants.DanmuState.en_onScreenBounding) {
                channelManeger.setIsChannelUsed(tempDanMakuSprite.danmakuType, tempDanMakuSprite.useDanmeChannel, true);
            }
        }

        //=====================新代码=======================//

        //遍历滚动等待弹幕列表，将其添加到滚动空闲管道
        Iterator<DanMaKu> waitingDanmakuListIterator = waitingDanMuList.iterator();
        while (waitingDanmakuListIterator.hasNext()) {
            DanMaKu danMaKu = waitingDanmakuListIterator.next();
            int freeChannelIndex = channelManeger.getFreeColumeIndex(danMaKu.getDanmuType());
            if (freeChannelIndex != -1) {
                Log.e(tag,"添加弹幕");
                DanMakuSprite danMakuSprite = DanMaKuSPFactory.createDanMaKuSP(danMaKu.getDanmuType(), freeChannelIndex, 0, (freeChannelIndex + 1) * (DanMaKuViewConstants.consentItemHeight + DanMaKuViewConstants.consentMargin), danMaKu.getDanmuColor(), curMills, danMaKu.getDanmakuContent());
                needToAddSpList.add(danMakuSprite);
                channelManeger.setIsChannelUsed(danMaKu.getDanmuType(), freeChannelIndex, true);
                waitingDanmakuListIterator.remove();
            }
        }

        //刷新全部弹幕位置
        for (DanMakuSprite danMakuSprite : showingDanmuSpList) {
            danMakuSprite.freshSprite(curMills);
        }

        //开始结束一帧的工作
        //从弹幕bitmap缓冲池中移除Bitmap
        for (DanMakuSprite sp : needToRemoveSpList) {
            bitmapManager.removeBitmap(sp.bmpId);
        }
        //从播放列表移出全部离开屏幕弹幕
        showingDanmuSpList.removeAll(needToRemoveSpList);
        //添加需要添加的弹幕到播放列表
        showingDanmuSpList.addAll(needToAddSpList);
        //获取本次移动的参数信息
        danMakusStatus.showingDanmakuCount = showingDanmuCount;
        danMakusStatus.waitingDanmakuCount = waitingDanMuList.size();
        danMakusStatus.removeDanmakuCount = needToRemoveSpList.size();
        danMakusStatus.addNewDanmakuCount = needToAddSpList.size();
        danMakusStatus.danmubmpCacheCount = bitmapManager.getBmpCacheSize();
        danMakusStatus.curTime = curMills;
        //清除全部弹幕缓存，共下一帧使用
        needToRemoveSpList.clear();
        needToAddSpList.clear();

        long methodEndTime = System.currentTimeMillis();
        //弹幕移动方法调用计时
        danMakusStatus.moveDanmakusUseTimeMills = methodEndTime - methodStartTime;
        Log.e(tag,String.format("当前屏幕弹幕：%s,添加弹幕数量：%s,移除弹幕数量：%d",danMakusStatus.showingDanmakuCount,danMakusStatus.addNewDanmakuCount,danMakusStatus.removeDanmakuCount));
        return danMakusStatus;
    }

    /**
     * 清除缓存中的弹幕，包括将要添加的顶部，滚动弹幕，等待中的顶部，滚动弹幕。供seek视频时使用
     */
    public void clearDanmuCache() {
        waitingDanMuList.clear();
        needToRemoveSpList.clear();
        needToAddSpList.clear();
    }

    public void addDanMu(DanMaKu danMaKu) {
        waitingDanMuList.add(danMaKu);
    }

    /**
     * 每一帧弹幕逻辑计算的状态信息
     */
    class DanMakusStatus{
        /**
         * 当前屏幕剩余弹幕数
         */
        public int showingDanmakuCount;
        /**
         * 等待弹幕数
         */
        public int waitingDanmakuCount;
        /**
         * 本次回收弹幕数量
         */
        public int removeDanmakuCount;
        /**
         * 本次新添加弹幕数量
         */
        public int addNewDanmakuCount;
        /**
         * 本次弹幕位置计算剩余Bitmap数量
         */
        public int danmubmpCacheCount;
        /**
         * 本次绘制开始时间
         */
        public long curTime;
        /**
         * 本次绘制用是
         */
        public long moveDanmakusUseTimeMills;
    }
}
