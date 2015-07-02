package com.cch.danmakuproj.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cch.danmakuproj.R;

/**
 * Created by 晨晖 on 2015-05-15.
 */
public class BufferingPopupWindow extends PopupWindow {

    private View bufferingBaseView = null;
    private LayoutInflater layoutInflater;
    private TextView tv_buffer_percent;

    private String tag = "BufferingPopupWindow";

    public BufferingPopupWindow(Context context) {
        super(context);
        layoutInflater = LayoutInflater.from(context);
        bufferingBaseView = layoutInflater.inflate(R.layout.popwin_buffering_layout, null);

        tv_buffer_percent = (TextView) bufferingBaseView.findViewById(R.id.tv_buffer_percent);
        setContentView(bufferingBaseView);
        setHeight(200);
        setWidth(200);
        Log.e(tag, "height = " + bufferingBaseView.getMeasuredHeight() + "width=" + bufferingBaseView.getMeasuredWidth());

    }

    public void setProgressPercent(int percent){
        tv_buffer_percent.setText(tv_buffer_percent.getResources().getString(R.string.is_loading)+percent+"%");
    }
    public BufferingPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


}
