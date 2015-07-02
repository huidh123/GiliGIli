package com.cch.danmakuproj.CustomView;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.cch.danmakuproj.Utils.Constants;
import com.cch.danmakuproj.javaBean.DanMaKu;
import com.cch.danmakuproj.javaBean.DanMuItem;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 弹幕管理器，只应该创建一次
 * Created by 晨晖 on 2015-04-11.
 */
public class DanMuManager {
    public static final String tag = "DanMuManager";

    //正在屏幕显示的滚动弹幕及其管理数据结构
    public ArrayList<DanMuItem> showingDanMuList = new ArrayList<>();
    public ArrayList<DanMaKu> waitingDanMuList = new ArrayList<DanMaKu>();

    private HashSet<DanMuItem> needToRemoveDanmuSet = new HashSet<DanMuItem>();
    private HashSet<DanMuItem> needToAddDanmuSet = new HashSet<DanMuItem>();

    //顶部弹幕及其管理数据结构
    private ArrayList<DanMuItem> showingTopDanmuList = new ArrayList<>();
    private ArrayList<DanMaKu> waitingTopDanmuList = new ArrayList<>();


    public int viewHeight = 0;
    public int viewWidth = 0;
    public int maxColumes = 0;
    /**
     * 滚动弹幕管道是否占用
     */
    private static boolean[] danmuChannel;
    /**
     * 顶部弹幕管道是否被占用
     */
    private static boolean[] topDanmuChannel;

    public Rect viewRect;
    public Context context;
    /**
     * 用于测量弹幕宽度的画笔
     */
    private Paint measureStrPaint;
    /**
     * 弹幕移动速度
     */
    private int speed = 10;
    /**
     * 顶部弹幕消失时间
     */
    private int topDanmuFadeTimeMills = 3000;
    /**
     * 管道宽度
     */
    private int consentItemHeight = 30;
    /**
     * 管道之间间隔
     */
    private int consentMargin = 5;
    private int judgeOffCon = 35;

    public enum DanmuState {
        en_outOfScreen,
        en_totallyInScreen,
        en_onScreenBounding
    }

    public DanMuManager() {

    }

    public void initDanMuManagerConfig(Context context, int viewHeight, int viewWidth, int maxColumes, int textSize) {
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
        this.maxColumes = maxColumes;
        this.context = context;
        measureStrPaint = new Paint();
        consentItemHeight = textSize + consentMargin;
        measureStrPaint.setTextSize(textSize);

        danmuChannel = new boolean[maxColumes];
        topDanmuChannel = new boolean[maxColumes];
        viewRect = new Rect(0, 0, viewWidth, viewHeight);
    }

    /**
     * 更新弹幕位置方法，在每一帧中调用
     */
    public void freshAllShowingDanMu(long curMills) {

        //对于正在显示的所有弹幕，更新弹幕管道状态，更改弹幕位置，更改弹幕状态
        for (int i = 0; i < danmuChannel.length; i++) {
            danmuChannel[i] = false;
        }
//        //对于正在显示的所有弹幕，更新弹幕管道状态，更改弹幕位置，更改弹幕状态
//        for (int i = 0; i < topDanmuChannel.length; i++) {
//            topDanmuChannel[i] = false;
//        }

        //Log.e(tag,"等待弹幕数量："+waitingDanMuList.size());
        int showingDanmuCount = showingDanMuList.size();
        for (int i = 0; i < showingDanmuCount; i++) {
            DanMuItem item = showingDanMuList.get(i);
            //更新滚动弹幕位置
            if (item.danmuType == Constants.DANMU_TYPE_RIGHT_TOLEFT) {
                if (judgeDanmuState(item) == DanmuState.en_outOfScreen) {
                    needToRemoveDanmuSet.add(item);
                } else if (judgeDanmuState(item) == DanmuState.en_onScreenBounding && danmuChannel[item.useDanmeChannel] == false) {
                    danmuChannel[item.useDanmeChannel] = true;
                }
                item.curX -= speed;
            }
            //更新顶部弹幕位置
            else if (item.danmuType == Constants.DANMU_TYPE_TOP) {
                //当播放时间大于弹幕出现时间时移除顶部弹幕
                if(curMills > (item.timeMs + topDanmuFadeTimeMills)){
                   needToRemoveDanmuSet.add(item);
                    topDanmuChannel[item.useDanmeChannel] = false;
                }
            }
            //未知类型弹幕
            else {
                showingDanMuList.remove(i);
                Log.e(tag, "未知的弹幕类型，已移除");
            }

        }

        //遍历滚动等待弹幕列表，将其添加到滚动空闲管道
        for (int i = 0; i < maxColumes; i++) {
            if (danmuChannel[i] == false) {
                if (waitingDanMuList.size() != 0) {
                    DanMaKu tempDanmu = waitingDanMuList.remove(0);
                    danmuChannel[i] = true;
                    DanMuItem danMuItem = createItemByDanMaKu(tempDanmu, i, viewWidth, (i + 1) * consentItemHeight);
                    // waitingDanMuList.remove(0);
                    needToAddDanmuSet.add(danMuItem);
                }
            }
        }

        //遍历顶部等待弹幕列表，将其添加到顶部空闲管道
        for (int i = 0; i < maxColumes; i++) {
            if (topDanmuChannel[i] == false) {
                if (waitingTopDanmuList.size() != 0) {
                    DanMaKu tempDanmu = waitingTopDanmuList.remove(0);
                    topDanmuChannel[i] = true;
                    int showX = getTopDanmuShowX(tempDanmu.getDanmakuContent());
                    DanMuItem danMuItem = createItemByDanMaKu(tempDanmu, i, showX, (i + 1) * consentItemHeight);
                    danMuItem.timeMs = (int) curMills;
                    needToAddDanmuSet.add(danMuItem);
                }
            }
        }

        //从播放列表移出全部离开屏幕弹幕
        showingDanMuList.removeAll(needToRemoveDanmuSet);
        //添加需要添加的弹幕到播放列表
        showingDanMuList.addAll(needToAddDanmuSet);
        if (needToRemoveDanmuSet.size() != 0 || needToAddDanmuSet.size() != 0) {
            //Log.e(tag, String.format("将要移除的弹幕数：%d,将要添加的弹幕数：%d", needToRemoveDanmuSet.size(), needToAddDanmuSet.size()));
        }
        //清除全部弹幕缓存，共下一帧使用
        needToRemoveDanmuSet.clear();
        needToAddDanmuSet.clear();
    }

    /**
     * 判断当前弹幕状态
     *
     * @param danMuItem
     * @return 弹幕状态 移出屏幕，完全屏幕内，屏幕边界
     */
    private DanmuState judgeDanmuState(DanMuItem danMuItem) {
        if (danMuItem.curX > viewWidth + judgeOffCon || danMuItem.curX < -((int) danMuItem.danmuLength + judgeOffCon)) {
            return DanmuState.en_outOfScreen;
        } else if (danMuItem.curX <= viewWidth + judgeOffCon && danMuItem.curX > (viewWidth - danMuItem.danmuLength - judgeOffCon)) {
            return DanmuState.en_onScreenBounding;
        }
        return DanmuState.en_totallyInScreen;
    }

    /**
     * 添加弹幕到待播放弹幕列表
     *
     * @param message
     */
    public void addDanMu(DanMaKu message) {
        if (message.getDanmuType() == Constants.DANMU_TYPE_RIGHT_TOLEFT) {
            addRtoLDanMu(message);
        } else if (message.getDanmuType() == Constants.DANMU_TYPE_TOP) {
            addTopDanMu(message);
        }
    }

    /**
     * 添加顶部弹幕
     *
     * @param message
     */
    private void addTopDanMu(DanMaKu message) {
        int danmuY = -1;
        int danmuColume = 0;
        for (danmuColume = 0; danmuColume < topDanmuChannel.length; danmuColume++) {
            if(!topDanmuChannel[danmuColume]){
                danmuY = (consentItemHeight+7)*(danmuColume +1);
                topDanmuChannel[danmuColume] = true;
                break;
            }
        }
        if(danmuY == -1){
            waitingTopDanmuList.add(message);
            return;
        }
        int showX = getTopDanmuShowX(message.getDanmakuContent());
        DanMuItem danMuItem = createItemByDanMaKu(message,danmuColume,showX,danmuY);
        showingDanMuList.add(danMuItem);
    }

    /**
     * 添加从右到左弹幕方法
     */
    private void addRtoLDanMu(DanMaKu message) {
        int danmuY = -1;
        int danmuColume = 0;
        for (danmuColume = 0; danmuColume < maxColumes; danmuColume++) {
            if (!danmuChannel[danmuColume]) {
                danmuY = consentItemHeight * (danmuColume + 1);
                danmuChannel[danmuColume] = true;
                break;
            }
        }
        //如果管道已满
        if (danmuY == -1) {
            waitingDanMuList.add(message);
            return;
        }
        DanMuItem danMuItem = createItemByDanMaKu(message, danmuColume, viewWidth, danmuY);
        showingDanMuList.add(danMuItem);
    }

    /**
     * 根据网络传回弹幕类创建显示弹幕
     * @param danMaKu
     * @param useDanmuChannel
     * @param showX
     * @param showY
     * @return
     */
    private DanMuItem createItemByDanMaKu(DanMaKu danMaKu, int useDanmuChannel, int showX, int showY) {
        if(danMaKu.getDanmuType() == Constants.DANMU_TYPE_TOP){

            Log.e(tag,"useDanmuChannel = "+useDanmuChannel);
        }
        DanMuItem danMuItem = new DanMuItem();
        danMuItem.message = danMaKu.getDanmakuContent();
        danMuItem.useDanmeChannel = useDanmuChannel;
        danMuItem.danmuLength = measureStrPaint.measureText(danMuItem.message);
        danMuItem.curX = showX;
        danMuItem.curY = showY;
        danMuItem.timeMs = danMaKu.getShowTime();
        danMuItem.danmuColor = danMaKu.getDanmuColor();
        danMuItem.danmuType = danMaKu.getDanmuType();
        return danMuItem;
    }

    /**
     * 根据字符长度计算顶部弹幕显示X坐标
     * @param danmuStr
     * @return
     */
    private int getTopDanmuShowX(final String danmuStr){
        if(danmuStr == null){
            return 0 ;
        }
        float danmuLength = measureStrPaint.measureText(danmuStr);
        int showX = (int) ((viewWidth - danmuLength) / 2);
        return showX;
    }

    /**
     * 清除缓存中的弹幕，包括将要添加的顶部，滚动弹幕，等待中的顶部，滚动弹幕。供seek视频时使用
     */
    public void clearDanmuCache(){
        waitingDanMuList.clear();
        needToRemoveDanmuSet.clear();
        needToAddDanmuSet.clear();
        waitingTopDanmuList.clear();
    }
}
