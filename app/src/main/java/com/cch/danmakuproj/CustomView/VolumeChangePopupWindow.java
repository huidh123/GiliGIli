package com.cch.danmakuproj.CustomView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cch.danmakuproj.AndroidUtils.DensityUtils;
import com.cch.danmakuproj.R;

/**
 * Created by 晨晖 on 2015-07-02.
 */
public class VolumeChangePopupWindow extends PopupWindow {

    private Context context;
    private LayoutInflater layoutInflater;
    private TextView tv_cur_volume;

    public  VolumeChangePopupWindow(Context context) {
        super(context);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        View contentView =layoutInflater.inflate(R.layout.popwin_volume_change_layout,null);
        tv_cur_volume = (TextView) contentView.findViewById(R.id.tv_cur_volume);
        this.setContentView(contentView);
        setHeight(DensityUtils.dp2px(context, 200));
        setWidth(DensityUtils.dp2px(context, 200));
    }

    public void setVolumeText(int curVolume,int maxVolume){
        tv_cur_volume.setText(curVolume+"/"+maxVolume);
    }
}
