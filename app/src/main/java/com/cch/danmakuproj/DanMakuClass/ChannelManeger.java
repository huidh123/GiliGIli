package com.cch.danmakuproj.DanMakuClass;

/**
 * Created by 晨晖 on 2015-07-31.
 */
public class ChannelManeger {

    private boolean [] RtoLChannels;
    private boolean [] TOPChannels;
    private int columes;

    public ChannelManeger(int columes){
        this.columes = columes;
        RtoLChannels = new boolean[columes];
        TOPChannels = new boolean[columes];
    }

    /**
     * 返回空闲管道序号,无空闲则返回-1
     * @param danmuType
     * @return
     */
    public int getFreeColumeIndex(int danmuType){
        boolean [] channel = null;
        if(danmuType == DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT){
            channel = RtoLChannels;
        }else if(danmuType == DanMaKuViewConstants.DANMU_TYPE_TOP){
            channel = TOPChannels;
        }
        if(channel != null){
            for(int i = 0 ; i < channel.length ; i++){
                if(!channel[i]){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 设置某个管道是否被占用
     * @param danmuType
     * @param index
     */
    public void setIsChannelUsed(int danmuType , int index , boolean isUse){
        boolean [] channel = null;
        if(danmuType == DanMaKuViewConstants.DANMU_TYPE_RIGHT_TOLEFT){
            channel = RtoLChannels;
        }else if(danmuType == DanMaKuViewConstants.DANMU_TYPE_TOP){
            channel = TOPChannels;
        }
        if(channel != null && index < channel.length){
            channel[index] = isUse;
        }

    }

    /**
     * 清除所有管道占用
     */
    public void clearAllChannelUser(){
        RtoLChannels = new boolean[columes];
        TOPChannels = new boolean[columes];
    }
}
