package com.cch.danmakuproj.DanMakuClass;

import android.util.Log;

/**
 * 创建弹幕精灵对象静态工厂
 * Created by 晨晖 on 2015-07-31.
 */
public class DanMaKuSPFactory {

    /**
     * 创建弹幕精灵对象
     *
     * @param danmakuType
     * @param useDanmeChannel
     * @param curX
     * @param curY
     * @param color
     * @param showTimeMS
     * @return
     */
    public static DanMakuSprite createDanMaKuSP(int danmakuType, int useDanmeChannel, int curX, int curY, String color, long showTimeMS, String message) {
        if (danmakuType == DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT) {
            RtoLDanMakuSP danMakuSprite = new RtoLDanMakuSP();
            danMakuSprite.curX = DanMaKuViewConstants.viewWidth;
            danMakuSprite.curY = curY;
            danMakuSprite.showTimeMS = showTimeMS;
            danMakuSprite.danmuColor = color;
            danMakuSprite.useDanmeChannel = useDanmeChannel;
            danMakuSprite.message = message;
            danMakuSprite.danmuLength = PaintManeger.measureDanMaKuLength(danMakuSprite);
            danMakuSprite.initDanmukuSp();
            danMakuSprite.calculateMoveSpeed();
            Log.e("measureLength", "弹幕长度：" + danMakuSprite.danmuLength);
            return danMakuSprite;
        } else if (danmakuType == DanMaKuViewConstants.DANMU_TYPE_TOP) {
            TopDanMakuSP danMakuSprite = new TopDanMakuSP();
            danMakuSprite.message = message;
            danMakuSprite.danmuLength = PaintManeger.measureDanMaKuLength(danMakuSprite);
            danMakuSprite.curX = (DanMaKuViewConstants.viewWidth - (int) danMakuSprite.danmuLength) / 2;
            danMakuSprite.curY = curY;
            danMakuSprite.showTimeMS = showTimeMS;
            danMakuSprite.danmuColor = color;
            danMakuSprite.useDanmeChannel = useDanmeChannel;
            danMakuSprite.initDanmukuSp();
            Log.e("measureLength", "弹幕长度：" + danMakuSprite.danmuLength);
            return danMakuSprite;
        } else {
            return null;
        }
    }

}
