package com.cch.danmakuproj.javaBean;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by 晨晖 on 2015-06-30.
 */
public class DanmuFileData {
    private String chatServer;
    private int chatId;
    private int mission;
    private int maxlimit;
    private String source;
    private ArrayList<DanMaKu> danmuList;

    public String getChatServer() {
        return chatServer;
    }

    public void setChatServer(String chatServer) {
        this.chatServer = chatServer;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getMission() {
        return mission;
    }

    public void setMission(int mission) {
        this.mission = mission;
    }

    public int getMaxlimit() {
        return maxlimit;
    }

    public void setMaxlimit(int maxlimit) {
        this.maxlimit = maxlimit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<DanMaKu> getDanmuList() {
        return danmuList;
    }

    public void setDanmuList(ArrayList<DanMaKu> danmuList) {
        this.danmuList = danmuList;
    }

    public DanmuFileData() {
    }
}
