package com.cch.danmakuproj.DanMakuClass;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 弹幕精灵父类
 * Created by 晨晖 on 2015-07-31.
 */
public abstract class DanMakuSprite {

    public String message;
    /**
     * 弹幕发送时间
     */
    public long showTimeMS;
    public float curX;
    public float curY;
    public String danmuColor;
    public int useDanmeChannel;
    public DanMaKuViewConstants.DanmuState danmuCurState;
    public float danmuLength;
    public int danmakuType;

    public String bmpId;

    private PaintManeger paintManeger;
    private BitmapManager bitmapManager;

    public DanMakuSprite() {
        bitmapManager = BitmapManager.getInstance();
        paintManeger = PaintManeger.getInstance();
        danmuCurState = DanMaKuViewConstants.DanmuState.en_totallyInScreen;
    }

    /**
     * 弹幕精灵二段构造
     */
    public void initDanmukuSp(){
        this.bmpId = bitmapManager.createDanmakuBmp((int) danmuLength,DanMaKuViewConstants.consentItemHeight, message, danmuColor);
    }
    public void draw(Canvas canvas) {
        if(bitmapManager.getBitmapById(bmpId) != null){
            canvas.drawBitmap(bitmapManager.getBitmapById(bmpId), curX, curY,paintManeger.bmpPaint);
        }
    }

    /**
     * 抽象方法，用于精灵自身的刷新
     */
    public abstract void freshSprite(long curMills);


    /**
     * 判断控件当前相对屏幕位置
     *
     * @return 精灵状态枚举
     */
    public DanMaKuViewConstants.DanmuState getDanMaKuStatus() {
        if (this.curX > DanMaKuViewConstants.viewWidth + DanMaKuViewConstants.judgeOffCon || this.curX < -(this.danmuLength + DanMaKuViewConstants.judgeOffCon)) {
            danmuCurState = DanMaKuViewConstants.DanmuState.en_outOfScreen;
            return DanMaKuViewConstants.DanmuState.en_outOfScreen;
        } else if (this.curX <= DanMaKuViewConstants.viewWidth + DanMaKuViewConstants.judgeOffCon && this.curX > (DanMaKuViewConstants.viewWidth - this.danmuLength - DanMaKuViewConstants.judgeOffCon)) {
            danmuCurState = DanMaKuViewConstants.DanmuState.en_onScreenBounding;
            return DanMaKuViewConstants.DanmuState.en_onScreenBounding;
        }
        danmuCurState = DanMaKuViewConstants.DanmuState.en_totallyInScreen;
        return DanMaKuViewConstants.DanmuState.en_totallyInScreen;
    }

}