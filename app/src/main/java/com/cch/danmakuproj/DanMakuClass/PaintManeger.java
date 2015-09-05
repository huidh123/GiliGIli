package com.cch.danmakuproj.DanMakuClass;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * 画笔管理类，管理画笔颜色
 * Created by 晨晖 on 2015-07-31.
 */
public class PaintManeger {
    private static Map<String, Paint> paintPool;

    /**
     * 用于测量弹幕宽度的画笔
     */
    public static Paint measureStrPaint;

    private static PaintManeger paintManeger;

    private PaintManeger() {
        paintPool = new HashMap<String, Paint>();
        measureStrPaint = new Paint();
        measureStrPaint.setTextSize(DanMaKuViewConstants.DANMAKU_TEXT_SIZE);
    }

    public synchronized static PaintManeger getInstance() {
        if (paintManeger == null) {
            paintManeger = new PaintManeger();
        }
        return paintManeger;
    }

    /**
     * 获取对应颜色值的画笔，若无则会新建画笔
     *
     * @param color 颜色字符串，类似#000000
     * @return
     */
    public Paint getPaintByColor(String color) {
        Paint paint = paintPool.get(color);
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.parseColor(color));
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setDither(true);
            paint.setTextSize(DanMaKuViewConstants.DANMAKU_TEXT_SIZE);
            paint.setShadowLayer(DanMaKuViewConstants.DANMAKU_STROKE_WIDTH, 0, 0, Color.BLACK);
            paintPool.put(color, paint);
        }
        return paint;
    }

    /**
     * 用于弹幕精灵宽度测量
     * @param danMakuSprite
     * @return
     */
    public static float  measureDanMaKuLength(DanMakuSprite danMakuSprite){
        float length = measureStrPaint.measureText(danMakuSprite.message);
        danMakuSprite.danmuLength = length;
        return length;
    }
}
