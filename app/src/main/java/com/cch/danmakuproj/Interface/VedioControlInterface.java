package com.cch.danmakuproj.Interface;

import android.widget.PopupWindow;

/**
 * 视频播放控制接口
 * Created by 晨晖 on 2015-07-02.
 */
public interface VedioControlInterface {
    /**
     * 继续播放
     */
     void startVideoPlay();

    /**
     * 暂停播放
     */
     void pauseVideoPlay();

    /**
     * 指定快进或是慢放时间
     * @param deltaMills 视频播放位移时间，大于零则加速，反之后退
     */
     void movePlayDuration(int deltaMills);

    /**
     * 更改音量大小
     * @param deltaVolume 音量大小绝对值
     */
     void changeVolume(int deltaVolume);

    /**
     * 更改亮度
     * @param deltaCount 亮度大小绝对值
     */
     void changeBrightness(int deltaCount);

    /**
     * 设置是否显示控制台View
     * @param isShow
     */
     void isShowControlView(boolean isShow);

     void showStatePopWin(PopupWindow window);
}
