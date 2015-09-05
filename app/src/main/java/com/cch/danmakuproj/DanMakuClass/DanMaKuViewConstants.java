package com.cch.danmakuproj.DanMakuClass;

import android.graphics.Paint;

/**
 * Created by 晨晖 on 2015-07-31.
 */
public class DanMaKuViewConstants {

    /**
     * 控件高度
     */
    public static int viewHeight;
    /**
     * 控件宽度
     */
    public static int viewWidth;
    /**
     * 弹幕描边宽度
     */
    public static final int DANMAKU_STROKE_WIDTH = 2;

    /**
     * 弹幕字体大小
     */
    public static final int DANMAKU_TEXT_SIZE = 60;

    /**
     * 弹幕相对屏幕状态
     */
    public enum DanmuState {
        en_outOfScreen,
        en_totallyInScreen,
        en_onScreenBounding
    }

    /**
     * 弹幕上下外边距
     */
    public static final int consentMargin = 10;

    /**
     * 管道宽度
     */
    public static final int consentItemHeight = DANMAKU_TEXT_SIZE;
    /**
     * 同行弹幕间距
     */
    public static final int judgeOffCon = 35;

    public static final int DANMAKU_ACROSS_TIMEMS = 3800;
    /**
     * 弹幕类型标示符
     */
    public static int DANMU_TYPE_RIGHT_TOLEFT = 1;
    public static int DANMU_TYPE_TOP = 4;
}