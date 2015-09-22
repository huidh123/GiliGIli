package com.cch.danmakuproj.DanMakuClass;

import android.util.Log;

/**
 * Created by 晨晖 on 2015-07-31.
 */
public class RtoLDanMakuSP extends DanMakuSprite {
    public float moveSpeed;

    public RtoLDanMakuSP() {
        //设置弹幕类型
        this.danmakuType = DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT;
        Log.e("speed", "弹幕速度" + moveSpeed + ",弹幕长度" + danmuLength);
    }

    public void calculateMoveSpeed() {
        this.moveSpeed = (this.danmuLength + DanMaKuViewConstants.judgeOffCon * 2 + DanMaKuViewConstants.viewWidth) / DanMaKuViewConstants.DANMAKU_ACROSS_TIMEMS;
    }

    @Override
    public void freshSprite(long curMills) {
        Log.e("RtoLDanMakuSP", String.format("弹幕内容：%s，弹幕速度：%s,当前状态：%s,当前位置:%s", this.message, this.moveSpeed, this.danmuCurState, this.curX));
        curX -= moveSpeed * 10;
    }
}
