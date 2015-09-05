package com.cch.danmakuproj.Utils;

import com.cch.danmakuproj.AndroidUtils.L;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by 晨晖 on 2015-06-30.
 */
public class DataUtils {
    /**
     * 根据输入流和handler解析xml文档成为对象
     * @param is 输入流。注意：解析完之后这个流会被关闭
     * @param defaultHandler 解析用handler
     * @return
     */
    public static DefaultHandler parseXMLToDanmakuFile(InputStream is,DefaultHandler defaultHandler){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(is,defaultHandler);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultHandler;
    }

    /**
     * 将数字型的颜色值转化为字符型，类似#FFFFFF
     * @param colorCode
     * @return
     */
    public static String getColorStrByInt(int colorCode){
        String resultColorStr = Integer.toHexString(colorCode);
        if(resultColorStr.length() < 6){
            int temoLength = resultColorStr.length();
            for(int i = 0 ; i < (6 - temoLength) ; i++){
                resultColorStr = "0" + resultColorStr;
            }
        }
        resultColorStr = "#"+resultColorStr;
        return resultColorStr;
    }

    /**
     * 获取两点之间距离长度
     * @param p1x
     * @param p1y
     * @param p2x
     * @param p2y
     * @return
     */
    public static double getDistanceBetweenTwoPoint(float p1x,float p1y,float p2x,float p2y){
        return Math.sqrt(Math.pow((p1x - p2x),2) + Math.pow((p1y - p2y),2));
    }
}
