package com.cch.danmakuproj.javaBean;

import com.cch.danmakuproj.Utils.Constants;

/**
 * Created by 晨晖 on 2015-04-25.
 */
public class DanMaKu {
    private String danmakuContent;
    private int showTime;
    private int danmuType;
    private String danmuColor;

    public String getDanmakuContent() {
        return danmakuContent;
    }

    public void setDanmakuContent(String danmakuContent) {
        this.danmakuContent = danmakuContent;
    }

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public int getDanmuType() {
        return danmuType;
    }

    public void setDanmuType(int danmuType) {
        this.danmuType = danmuType;
    }

    public String getDanmuColor() {
        return danmuColor;
    }

    public void setDanmuColor(String danmuColor) {
        this.danmuColor = danmuColor;
    }

    public DanMaKu(String danmakuContent, int showTime, int danmuType, String danmuColor) {
        this.danmakuContent = danmakuContent;
        this.showTime = showTime;
        this.danmuType = danmuType;
        this.danmuColor = danmuColor;
    }

    public DanMaKu(String danmakuContent, int showTime) {
        this.danmakuContent = danmakuContent;
        this.showTime = showTime;
        danmuType = Constants.DANMU_TYPE_RIGHT_TOLEFT;
        danmuColor = "#FFFFFF";
    }

    public DanMaKu() {
    }
}
