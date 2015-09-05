package com.cch.danmakuproj.DanMakuClass;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.cch.danmakuproj.Utils.Constants;
import com.cch.danmakuproj.Utils.DebugUtils;
import com.cch.danmakuproj.javaBean.DanMaKu;
import com.cch.danmakuproj.javaBean.DanMuItem;

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
    public Context context;

    public DanMaKuManeger() {

    }

    public void initDanMuManagerConfig(Context context, int viewHeight, int viewWidth, int maxColumes) {
        DanMaKuViewConstants.viewHeight = viewHeight;
        DanMaKuViewConstants.viewWidth = viewWidth;
        this.maxColumes = maxColumes;
        this.context = context;
        channelManeger = new ChannelManeger(maxColumes);
    }

    /**
     * 更新弹幕位置方法，在每一帧中调用
     */
    public void freshAllShowingDanMu(long curMills) {

        channelManeger.clearAllChannelUser();
        int showingDanmuCount = showingDanmuSpList.size();
        //Log.e("tag","当前时间:"+curMills+",showingDanmuCount:"+showingDanmuCount);
        //Log.e("tag","waitingDanMuList:"+waitingDanMuList.size()+",viewWidth:"+DanMaKuViewConstants.viewWidth+",viewHehgt:"+DanMaKuViewConstants.viewHeight);
        //=====================新代码=======================//

        for (int i = 0; i < showingDanmuCount; i++) {
            DanMakuSprite tempDanMakuSprite = showingDanmuSpList.get(i);
            //Log.e("showingDanmu","message:"+tempDanMakuSprite.message+"length:"+tempDanMakuSprite.danmuLength);
            //刷新全部弹幕位置
            tempDanMakuSprite.freshSprite(curMills);
            //获取弹幕与屏幕位置关系
            DanMaKuViewConstants.DanmuState curDanmuState = tempDanMakuSprite.getDanMaKuStatus();
            //移除离开屏幕的弹幕
            if (curDanmuState == DanMaKuViewConstants.DanmuState.en_outOfScreen) {
                Log.e("showingDanmu","移除:"+tempDanMakuSprite.message);
                needToRemoveSpList.add(tempDanMakuSprite);
            }else if(curDanmuState == DanMaKuViewConstants.DanmuState.en_onScreenBounding){
                channelManeger.setIsChannelUsed(tempDanMakuSprite.danmakuType,tempDanMakuSprite.useDanmeChannel,true);
            }
        }

        //=====================新代码=======================//

        //遍历滚动等待弹幕列表，将其添加到滚动空闲管道
        Iterator<DanMaKu> waitingDanmakuListIterator = waitingDanMuList.iterator();

        while (waitingDanmakuListIterator.hasNext()){
            DanMaKu danMaKu =  waitingDanmakuListIterator.next();
            int freeChannelIndex = channelManeger.getFreeColumeIndex(danMaKu.getDanmuType());
            if(freeChannelIndex != -1){
                DanMakuSprite danMakuSprite = DanMaKuSPFactory.createDanMaKuSP(danMaKu.getDanmuType(), freeChannelIndex, 0,
                        (freeChannelIndex + 1) * (DanMaKuViewConstants.consentItemHeight + DanMaKuViewConstants.consentMargin),
                        danMaKu.getDanmuColor(), curMills, danMaKu.getDanmakuContent());
                needToAddSpList.add(danMakuSprite);
                channelManeger.setIsChannelUsed(danMaKu.getDanmuType(), freeChannelIndex, true);
                waitingDanmakuListIterator.remove();
            }
        }
        for(DanMakuSprite danMakuSprite : showingDanmuSpList){
            danMakuSprite.freshSprite(curMills);
        }
        //从播放列表移出全部离开屏幕弹幕
        showingDanmuSpList.removeAll(needToRemoveSpList);
        //添加需要添加的弹幕到播放列表
        showingDanmuSpList.addAll(needToAddSpList);
        //清除全部弹幕缓存，共下一帧使用
        needToRemoveSpList.clear();
        needToAddSpList.clear();

    }

    /**
     * 清除缓存中的弹幕，包括将要添加的顶部，滚动弹幕，等待中的顶部，滚动弹幕。供seek视频时使用
     */
    public void clearDanmuCache(){
        waitingDanMuList.clear();
        needToRemoveSpList.clear();
        needToAddSpList.clear();
    }

    public void addDanMu(DanMaKu danMaKu) {
        waitingDanMuList.add(danMaKu);
    }
}
