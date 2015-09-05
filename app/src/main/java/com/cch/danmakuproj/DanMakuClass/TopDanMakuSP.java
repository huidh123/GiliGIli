package com.cch.danmakuproj.DanMakuClass;

import com.cch.danmakuproj.Utils.Constants;

/**
 * Created by 晨晖 on 2015-08-29.
 */
public class TopDanMakuSP extends DanMakuSprite {

    public TopDanMakuSP() {
        this.danmakuType = DanMaKuViewConstants.DANMU_TYPE_TOP;
    }

    @Override
    public void freshSprite(long curMills) {
        if((curMills - this.showTimeMS ) > DanMaKuViewConstants.DANMAKU_ACROSS_TIMEMS){
            this.danmuCurState = DanMaKuViewConstants.DanmuState.en_outOfScreen;
        }else{
            this.danmuCurState = DanMaKuViewConstants.DanmuState.en_onScreenBounding;
        }
    }

    @Override
    public DanMaKuViewConstants.DanmuState getDanMaKuStatus() {
        return this.danmuCurState;
    }
}
