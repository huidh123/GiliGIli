package com.cch.danmakuproj.Interface;

/**
 * Created by 晨晖 on 2015-05-14.
 */
public interface Publisher {
    public void addObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyAllObserver();
}
