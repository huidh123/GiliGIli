package com.cch.danmakuproj.CustomView;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.cch.danmakuproj.AndroidUtils.L;
import com.cch.danmakuproj.AndroidUtils.ScreenUtils;
import com.cch.danmakuproj.Interface.VedioControlInterface;
import com.cch.danmakuproj.Interface.VedioPlayStateInterface;
import com.cch.danmakuproj.Utils.DataUtils;

import io.vov.vitamio.widget.VideoView;

/**
 * Created by 晨晖 on 2015-07-02.
 */
public class VedioPlayGestureControl extends GestureDetector.SimpleOnGestureListener {

    private VedioControlInterface vedioControlInterface;
    private VedioPlayStateInterface vedioPlayStateInterface;
    private Context context;
    private VolumeChangePopupWindow volumeChangePopupWindow;
    private static final int fillpingMinDistance = 60;
    private int screenWidth = 0;
    private int screenHeight = 0;

    private enum FillpingKind{
        EN_VOLUME,
        EN_BRITNESS,
        EN_DURATION
    }
    private FillpingKind curOperationKind;

    public VedioPlayGestureControl(Context context, VedioPlayStateInterface vedioPlayStateInterface ,VedioControlInterface vedioControlInterface){
        this.context = context;
        this.vedioControlInterface = vedioControlInterface;
        this.vedioPlayStateInterface = vedioPlayStateInterface;
        volumeChangePopupWindow = new VolumeChangePopupWindow(context);
        screenWidth = ScreenUtils.getScreenWidth(context);
        screenHeight = ScreenUtils.getScreenHeight(context);
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //L.e("滑动屏幕 e1坐標：(" + e1.getX() + "," + e1.getY() + ")||e2坐標(" + e2.getX() + "," + e2.getY() + ")");
        vedioControlInterface.showStatePopWin(volumeChangePopupWindow);
        //当滑动距离小于50像时判断用户操作趋势
        if(DataUtils.getDistanceBetweenTwoPoint(e1.getX(),e1.getY(),e2.getX(),e2.getY()) <= 50){
            //当横向滑动时。移动播放时间
            if((Math.abs(e1.getX() - e2.getX())) > (Math.abs(e1.getY() - e2.getY()))){
                curOperationKind = FillpingKind.EN_DURATION;
            }
            //当下落位置位于屏幕左端时，且趋近于纵向滑动时。调节音量
            else if(e1.getX() < (screenWidth / 2)){
                curOperationKind = FillpingKind.EN_VOLUME;
            }
            //当下落位置位于屏幕右端时，且趋近于纵向滑动时。调节亮度
            else if(e1.getX() >= (screenWidth / 2)){
                curOperationKind = FillpingKind.EN_BRITNESS;
            }
        }
        //判断完趋势时候开始响应操作
        else{
            switch (curOperationKind){
                case EN_DURATION:
                    L.e("进度调节");
                    break;
                case EN_BRITNESS:
                    L.e("亮度调节");
                    break;
                case EN_VOLUME:
                    L.e("音量调节");
                    break;
            }
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        L.e("双击屏幕");
        if (vedioPlayStateInterface.getIsVideoPlaying()) {
            vedioControlInterface.pauseVideoPlay();
        } else {
            vedioControlInterface.startVideoPlay();
        }
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        L.e("单击屏幕");
        if(vedioPlayStateInterface.getIsControlViewShown()){
            vedioControlInterface.isShowControlView(false);
        }else{
            vedioControlInterface.isShowControlView(true);
        }
        return super.onSingleTapConfirmed(e);
    }
}
