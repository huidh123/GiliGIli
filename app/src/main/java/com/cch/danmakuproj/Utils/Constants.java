package com.cch.danmakuproj.Utils;

import com.cch.danmakuproj.javaBean.DanMaKu;

import java.util.ArrayList;

/**
 * Created by 晨晖 on 2015-04-11.
 */
public class Constants {
    //全局区域，弹幕播放器播放的弹幕列表
    public static ArrayList<DanMaKu> danmuList = new ArrayList<DanMaKu>();

    public static int vedioTimeLength = 60 * 1000;

    public static int DANMU_TYPE_RIGHT_TOLEFT = 1;
    public static int DANMU_TYPE_TOP = 4;

//    static {
//        danmuList.add(new DanMaKu("测1试幕", 0));
//        danmuList.add(new DanMaKu("测2试用第二条弹幕", 10));
//        danmuList.add(new DanMaKu("测3试用第三条弹幕第三条弹", 20));
//        danmuList.add(new DanMaKu("测4试用第四条弹幕第四条", 30));
//        danmuList.add(new DanMaKu("测5试用第五条弹幕", 40));
//        danmuList.add(new DanMaKu("测6试用第六条弹幕", 50));
//        danmuList.add(new DanMaKu("测7试用第七条弹幕用第一条弹幕", 60));
//        danmuList.add(new DanMaKu("测8试用第一条弹幕第四条", 501));
//        danmuList.add(new DanMaKu("测9试用第弹幕", 502));
//        danmuList.add(new DanMaKu("测10试用第一条弹幕第四条", 503));
//        danmuList.add(new DanMaKu("测11试用第一条弹幕", 504));
//        danmuList.add(new DanMaKu("测12试用第一条弹幕", 505));
//        danmuList.add(new DanMaKu("测13试用第一条弹幕第四条", 506));
//        danmuList.add(new DanMaKu("测14试用第一条弹幕", 1000));
//        danmuList.add(new DanMaKu("测15试用第一条弹幕", 1200));
//        danmuList.add(new DanMaKu("测16试用第一用第一条弹幕条弹幕用第一条弹幕测16试用第一用第一条弹幕条弹幕用第一条弹幕测16试用第一用第一条弹幕条弹幕用第一条弹幕测16试用第一用第一条弹幕条弹幕用第一条弹幕测16试用第一用第一条弹幕条弹幕用第一条弹幕测16试用第一用第一条弹幕条弹幕用第一条弹幕", 1200, Constants.DANMU_TYPE_RIGHT_TOLEFT, "#707070"));
//        danmuList.add(new DanMaKu("测17试用第一条弹幕", 1200));
//        danmuList.add(new DanMaKu("测18试用第一条弹幕", 1500));
//        danmuList.add(new DanMaKu("测19试用第一用第一条弹幕条弹幕", 2000));
//        danmuList.add(new DanMaKu("测20试用第一条弹一条弹幕", 2200));
//        danmuList.add(new DanMaKu("测21试用第一条弹幕", 2200));
//        danmuList.add(new DanMaKu("测22试用第一条用第一条弹幕弹幕", 2500));
//        danmuList.add(new DanMaKu("测23试幕", 3000));
//        danmuList.add(new DanMaKu("测24试用第一条弹幕", 3200));
//        danmuList.add(new DanMaKu("测25试用第用第一条弹幕一条弹幕", 4200));
//        danmuList.add(new DanMaKu("测26试用第一条弹幕", 4200));
//        danmuList.add(new DanMaKu("测27试用第一用第一条弹幕条弹幕", 4500));
//        danmuList.add(new DanMaKu("测28试用第一条弹幕", 5000));
//        danmuList.add(new DanMaKu("测29试用第一条弹幕", 5200));
//        danmuList.add(new DanMaKu("测30试用幕", 5200));
//        danmuList.add(new DanMaKu("测31试用第一条弹幕", 5200));
//        danmuList.add(new DanMaKu("测32试用第一条弹幕", 5500));
//
//        danmuList.add(new DanMaKu("测33试用第幕", 6200));
//        danmuList.add(new DanMaKu("测34试用第一条弹幕", 6500));
//        danmuList.add(new DanMaKu("测35试用第一条弹幕", 6600));
//        danmuList.add(new DanMaKu("测36试用第一条第一条弹弹幕", 6700));
//        danmuList.add(new DanMaKu("测37试用第一条弹幕", 6800));
//        danmuList.add(new DanMaKu("测38试用第一条弹幕", 6900));
//        danmuList.add(new DanMaKu("测39试用第一条弹幕", 6900));
//        danmuList.add(new DanMaKu("测40试用第一第一条弹条弹幕", 6900));
//        danmuList.add(new DanMaKu("测41试用第一条弹幕", 6900));
//        danmuList.add(new DanMaKu("测42试用第一条弹幕", 6900));
//        danmuList.add(new DanMaKu("测43试用第一条第一条弹弹幕", 6900));
//        danmuList.add(new DanMaKu("测44试用第一条弹幕", 6900));
//
//
//        danmuList.add(new DanMaKu("测45试用第2条弹幕", 11200,Constants.DANMU_TYPE_TOP,"#FF0000"));
//        danmuList.add(new DanMaKu("测46试用第2条弹幕", 11500,Constants.DANMU_TYPE_TOP,"#FF0000"));
//        danmuList.add(new DanMaKu("测47试用第2条弹幕", 11600,Constants.DANMU_TYPE_TOP,"#FF0000"));
//        danmuList.add(new DanMaKu("测48试用第2条弹幕用第2条弹", 13700,Constants.DANMU_TYPE_TOP,"#FF0000"));
//
//        danmuList.add(new DanMaKu("测49试用第2条弹幕", 16200,Constants.DANMU_TYPE_TOP,"#FF0000"));
//        danmuList.add(new DanMaKu("测50试用第2条弹幕", 16500));
//        danmuList.add(new DanMaKu("测51试用第2条弹幕", 16600));
//        danmuList.add(new DanMaKu("测52试用第2条弹幕用第2条弹", 16700));
//        danmuList.add(new DanMaKu("测53试用第2条弹幕", 16800));
//        danmuList.add(new DanMaKu("测54试用第2条弹幕用第2条弹", 16900));
//        danmuList.add(new DanMaKu("测55试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测56试用第2条弹幕用第2条弹", 16900));
//        danmuList.add(new DanMaKu("测57试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测58试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测59试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测60试用第2条弹幕", 16900));
//
//        danmuList.add(new DanMaKu("测61试用第2条弹幕", 16500));
//        danmuList.add(new DanMaKu("测62试用第2条弹幕", 16600));
//        danmuList.add(new DanMaKu("测63试用第2条弹幕用第2条弹", 16700));
//        danmuList.add(new DanMaKu("测64试用第2条弹幕", 16800));
//        danmuList.add(new DanMaKu("测65试用第2条弹幕用第2条弹", 16900));
//        danmuList.add(new DanMaKu("测66试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测67试用第2条弹幕用第2条弹", 16900));
//        danmuList.add(new DanMaKu("测68试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测69试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测70试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测71试用第2条弹幕", 16900));
//
//
//        danmuList.add(new DanMaKu("测72试用第2条弹幕", 16500));
//        danmuList.add(new DanMaKu("测73试用第2条弹幕", 16600));
//        danmuList.add(new DanMaKu("测74试用第2条弹幕用第2条弹", 16700));
//        danmuList.add(new DanMaKu("测75试用第2条弹幕", 16800));
//        danmuList.add(new DanMaKu("测76试用第2条弹幕用第2条弹", 16900));
//        danmuList.add(new DanMaKu("测77试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测78试用第2条弹幕用第2条弹", 16900));
//        danmuList.add(new DanMaKu("测79试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测80试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测81试用第2条弹幕", 16900));
//        danmuList.add(new DanMaKu("测82试用第2条弹幕", 16900));
//
//
//        danmuList.add(new DanMaKu("测83试用第2条弹幕", 17500));
//        danmuList.add(new DanMaKu("测84试用第2条弹幕", 17600));
//        danmuList.add(new DanMaKu("测85试用第2条弹幕用第2条弹", 17700));
//        danmuList.add(new DanMaKu("测86试用第2条弹幕", 18800));
//        danmuList.add(new DanMaKu("测87试用第2条弹幕用第2条弹", 18900));
//        danmuList.add(new DanMaKu("测88试用第2条弹幕", 18900));
//        danmuList.add(new DanMaKu("测89试用第2条弹幕用第2条弹", 18900));
//        danmuList.add(new DanMaKu("测90试用第2条弹幕", 18900));
//        danmuList.add(new DanMaKu("测91试用第2条弹幕", 20900));
//        danmuList.add(new DanMaKu("测92试用第2条弹幕", 20900));
//        danmuList.add(new DanMaKu("测93试用第2条弹幕", 20900));
//
//        danmuList.add(new DanMaKu("测94试用第2条弹幕", 21500));
//        danmuList.add(new DanMaKu("测95试用第2条弹幕", 23600));
//        danmuList.add(new DanMaKu("测96试用第2条弹幕用第2条弹", 25700));
//        danmuList.add(new DanMaKu("测97试用第2条弹幕", 25800));
//        danmuList.add(new DanMaKu("测98试用第2条弹幕用第2条弹", 26900));
//        danmuList.add(new DanMaKu("测99试用第2条弹幕", 26900));
//        danmuList.add(new DanMaKu("测100试用第2条弹幕用第2条弹", 26900));
//        danmuList.add(new DanMaKu("测101试用第2条弹幕", 26900));
//        danmuList.add(new DanMaKu("测102试用第2条弹幕", 26900));
//        danmuList.add(new DanMaKu("测103试用第2条弹幕", 26900));
//        danmuList.add(new DanMaKu("测104试用第2条弹幕", 26900));
//    }
}
