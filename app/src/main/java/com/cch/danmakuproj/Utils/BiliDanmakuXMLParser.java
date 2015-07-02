package com.cch.danmakuproj.Utils;

import android.util.Log;

import com.cch.danmakuproj.AndroidUtils.L;
import com.cch.danmakuproj.javaBean.DanMaKu;
import com.cch.danmakuproj.javaBean.DanmuFileData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by 晨晖 on 2015-06-30.
 */
public class BiliDanmakuXMLParser extends DefaultHandler {
    private DanmuFileData danmuFile;
    private DanMaKu tempDanmuObj;
    private ArrayList<DanMaKu> danMaKuList;

    private static final int elem_chatServer = 1;
    private static final int elem_chatId = 2;
    private static final int elem_mission = 3;
    private static final int elem_maxlimit = 4;
    private static final int elem_source = 5;
    private static final int elem_danmuList = 6;

    private int curState;

    private String tag = "BiliDanmakuXMLParser";

    private StringBuilder bufferTemp;

    public BiliDanmakuXMLParser(){

    }

    public DanmuFileData getDanmakuFileData(){
        return danmuFile;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        danmuFile = new DanmuFileData();
        danMaKuList = new ArrayList<DanMaKu>();
        bufferTemp = new StringBuilder();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        danmuFile.setDanmuList(danMaKuList);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(localName.equals("chatserver")){
            curState =elem_chatServer;
        }else if(localName.equals("chatid")){
            curState =elem_chatId;
        }else if(localName.equals("mission")){
            curState =elem_mission;
        }else if(localName.equals("maxlimit")){
            curState =elem_maxlimit;
        }else if(localName.equals("source")){
            curState =elem_source;
        }else if(localName.equals("d")){
            tempDanmuObj = createDanMakuAndParse(attributes.getValue("p"));
            curState = elem_danmuList;
        }
        bufferTemp.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch (curState){
            case elem_chatId:
                danmuFile.setChatId(Integer.parseInt(bufferTemp.toString()));
                break;
            case elem_chatServer:
                danmuFile.setChatServer(bufferTemp.toString());
                break;
            case elem_maxlimit:
                danmuFile.setMaxlimit(Integer.parseInt(bufferTemp.toString()));
                break;
            case elem_mission:
                danmuFile.setMission(Integer.parseInt(bufferTemp.toString()));
                break;
            case elem_source:
                danmuFile.setSource(bufferTemp.toString());
                break;
            case elem_danmuList:
                tempDanmuObj.setDanmakuContent(bufferTemp.toString());
                //L.e("解析弹幕内容" + bufferTemp.toString() + "当前顺序：" + danMaKuList.size());
                danMaKuList.add(tempDanmuObj);
                break;
            default:
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        bufferTemp.append(ch,start,length);
    }

    public DanMaKu createDanMakuAndParse(String danmuProperty){
        tempDanmuObj = new DanMaKu();
        String [] propertys = danmuProperty.split(",");
        tempDanmuObj.setShowTime((int) (Float.parseFloat(propertys[0]) * 1000));
        tempDanmuObj.setDanmuType(Integer.parseInt(propertys[1]));
        tempDanmuObj.setDanmuColor(DataUtils.getColorStrByInt(Integer.parseInt(propertys[3])));
        return tempDanmuObj;
    }
}
