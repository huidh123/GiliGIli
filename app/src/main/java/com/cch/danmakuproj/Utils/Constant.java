package com.cch.danmakuproj.Utils;

import com.cch.danmakuproj.AndroidUtils.SDCardUtils;

/**
 * Created by 晨晖 on 2015-05-14.
 */
public class Constant {
    public static final String SERVER_URL = "http://192.168.253.1:8080/DanMakuProj";
    public static final String VIDEO_URL = SERVER_URL + "/3622651-1.flv";

    public static final String GET_CHAT_FILE_SERVER_URL = "http://comment.bilibili.com/%s.xml";

    public static final String DANMU_FILE_SAVE_PATH = SDCardUtils.getSDCardPath()+"/GiliGili/";;
    /**
     * 将毫秒数转化为字符显示
     * @param millisecond
     * @return
     */
    public static String changeMSTimeToStr(long millisecond) {
        long seconds = millisecond / 1000;
        long minute = seconds / 60;
        long second = seconds % 60;
        String resStr = minute + ":" + second;
        return resStr;
    }

}
