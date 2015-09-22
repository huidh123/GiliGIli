package com.cch.danmakuproj.DanMakuClass;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.HashMap;
import java.util.UUID;

/**
 * 弹幕图片的Bitmap缓冲池,用于保存弹幕的绘制图片
 * Created by 晨晖 on 2015-09-19.
 */
public class BitmapManager {
    private static BitmapManager bitmapManager;
    private HashMap<String, Bitmap> bitmapPoolMap;

    private PaintManeger paintManeger;

    private BitmapManager() {
        bitmapPoolMap = new HashMap<String, Bitmap>();
        this.paintManeger = PaintManeger.getInstance();
    }

    public static synchronized BitmapManager getInstance() {
        if (bitmapManager == null) {
            bitmapManager = new BitmapManager();
        }
        return bitmapManager;
    }

    /**
     * 创建一个弹幕的缓存Bitmap，以后只需要绘制这个Bitmap即可
     *
     * @param height
     * @param width
     * @param message
     * @return 弹幕Bitmap的标识符(UUID计算)
     */
    public String createDanmakuBmp(int width, int height, String message, String color) {
        if(height <= 0 || width <= 0){
            Log.e("BitmapManager","弹幕参数错误，弹幕："+message);
            return "ERROR";
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Log.e("BitmapManager", String.format("createBitmap height :%d , width = %d", height, width));
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.argb(70, 100, 0, 0));
        //由于直接绘制text是基于基准线的，需要有6px的偏移
        canvas.drawText(message, 0, height - 6, paintManeger.getPaintByColor(color));
        String key = UUID.randomUUID().toString();
        bitmapPoolMap.put(key,bitmap);
        return key;
    }

    public Bitmap getBitmapById(String id) {
        return bitmapPoolMap.get(id);
    }

    public void removeBitmap(String bmpId) {
        bitmapPoolMap.remove(bmpId);
    }

    public int getBmpCacheSize(){
        return bitmapPoolMap.size();
    }
}
