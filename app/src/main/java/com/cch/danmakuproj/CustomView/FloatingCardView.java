package com.cch.danmakuproj.CustomView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 晨晖 on 2015-07-03.
 */
public class FloatingCardView extends View {

    private float cardViewCenterX;
    private float cardViewCenterY;
    private float cardViewWidth;
    private float cardViewHeight;
    private float cardViewTLX;
    private float cardViewTLY;

    private static float BORDER_WIDTH = 0;
    private static int ANIMA_DURATION = 200;
    private static int  ROTATION_RA = 5;


    private ObjectAnimator pressAnima;
    private PressDirection curPressDirection;
    private enum PressDirection{
        EN_TOP,EN_BOTTOM,EN_LEFT,EN_RIGHT,EN_TL,EN_TR,EN_BL,EN_BR,EN_CENTER
    }

    public FloatingCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pressAnima = ObjectAnimator.ofFloat(this,"PRESSANIMA",0f,1f);
        pressAnima.setDuration(ANIMA_DURATION);
        pressAnima.addUpdateListener(new PressAnimaUpdateListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        cardViewWidth = getMeasuredWidth();
        cardViewHeight = getMeasuredHeight();
        cardViewTLX = this.getX();
        cardViewTLY = this.getY();
        cardViewCenterX = (int) (getX() + cardViewWidth / 2);
        cardViewCenterY = (int) (getY() + cardViewHeight / 2);
        BORDER_WIDTH = cardViewWidth / 3;
        //L.e(String.format("View坐标(%s,%s)||View宽高(%s,%s)",  this.getX(),this.getY(),cardViewWidth,cardViewHeight));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当按下时判断压力方向
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float touchDownX = event.getX();
            float touchDownY = event.getY();
            curPressDirection = getPressDirect(touchDownX, touchDownY);
        }
        pressAnima.start();
        return super.onTouchEvent(event);
    }

    /**
     * 判断点击区域
     * @param touchX
     * @param touchY
     * @return
     */
    private PressDirection getPressDirect(float touchX,float touchY){
        PressDirection pressDirection  = null;
        //左上角
        if(isInPointArea(touchX,touchY,0,0,BORDER_WIDTH,BORDER_WIDTH)){
            pressDirection = PressDirection.EN_TL;
        }
        //上方
        else if(isInPointArea(touchX,touchY,BORDER_WIDTH , 0,BORDER_WIDTH,BORDER_WIDTH)){
            pressDirection = PressDirection.EN_TOP;
        }
        //右上角
        else if(isInPointArea(touchX,touchY,cardViewWidth-BORDER_WIDTH,0,BORDER_WIDTH,BORDER_WIDTH)){
            pressDirection = PressDirection.EN_TR;
        }
        //左方
        else if(isInPointArea(touchX,touchY,0,BORDER_WIDTH,BORDER_WIDTH,BORDER_WIDTH)){
            pressDirection = PressDirection.EN_LEFT;
        }
        //右方
        else if(isInPointArea(touchX,touchY,cardViewWidth-BORDER_WIDTH,BORDER_WIDTH,BORDER_WIDTH,BORDER_WIDTH)){
            pressDirection = PressDirection.EN_RIGHT;
        }
        //左下
        else if(isInPointArea(touchX,touchY,0,cardViewHeight-BORDER_WIDTH,BORDER_WIDTH,BORDER_WIDTH)){
            pressDirection = PressDirection.EN_BL;
        }
        //下
        else if(isInPointArea(touchX,touchY,BORDER_WIDTH ,cardViewHeight-BORDER_WIDTH,BORDER_WIDTH,BORDER_WIDTH)){
            pressDirection = PressDirection.EN_BOTTOM;
        }
        //右下
        else if(isInPointArea(touchX,touchY,cardViewWidth-BORDER_WIDTH ,cardViewHeight-BORDER_WIDTH,BORDER_WIDTH,BORDER_WIDTH)){
            pressDirection = PressDirection.EN_BR;
        }
        //中
        else{
            pressDirection = PressDirection.EN_CENTER;
        }
        return pressDirection;
    }

    /**
     * 判断点是否在区域内
     * @param pointX
     * @param pointY
     * @param areaX
     * @param areaY
     * @param areaWidth
     * @param areaHeight
     * @return
     */
    private boolean isInPointArea(float pointX,float pointY,float areaX,float areaY,float areaWidth,float areaHeight){
        if(pointX >= areaX && pointX <= (areaX + areaWidth) && pointY >= areaY && pointY <= (areaY + areaHeight)){
            return true;
        }else{
            return false;
        }
    }

    class PressAnimaUpdateListener implements ValueAnimator.AnimatorUpdateListener{

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float curValue = (float) valueAnimator.getAnimatedValue("PRESSANIMA");
            int tempRotationX = 0;
            int tempRotationY = 0;

            switch (curPressDirection){
                case EN_BL:
                    tempRotationX = -ROTATION_RA;
                    tempRotationY = -ROTATION_RA;
                    break;
                case EN_BOTTOM:
                    tempRotationX = -ROTATION_RA;
                    tempRotationY = 0;
                    break;
                case EN_BR:
                    tempRotationX = -ROTATION_RA;
                    tempRotationY = ROTATION_RA;
                    break;
                case EN_CENTER:
                    break;
                case EN_LEFT:
                    tempRotationX = 0;
                    tempRotationY = -ROTATION_RA;
                    break;
                case EN_RIGHT:
                    tempRotationX = 0;
                    tempRotationY = ROTATION_RA;
                    break;
                case EN_TL:
                    tempRotationX = ROTATION_RA;
                    tempRotationY = -ROTATION_RA;
                    break;
                case EN_TOP:
                    tempRotationX = 0;
                    tempRotationY = -ROTATION_RA;
                    break;
                case EN_TR:
                    tempRotationX = ROTATION_RA;
                    tempRotationY = ROTATION_RA;
                    break;
                default:
                    break;
            }
            FloatingCardView.this.setRotationX(tempRotationX * curValue);
            FloatingCardView.this.setRotationY(tempRotationY * curValue);
        }
    }
}
