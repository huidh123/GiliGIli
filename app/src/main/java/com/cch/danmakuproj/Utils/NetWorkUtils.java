package com.cch.danmakuproj.Utils;

import android.content.Context;
import android.util.Log;

import com.cch.danmakuproj.AndroidUtils.L;
import com.cch.danmakuproj.AndroidUtils.SDCardUtils;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.DeflateInputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;


/**
 * Created by 晨晖 on 2015-06-30.
 */
public class NetWorkUtils {

    private static  String tag = "NetWorkUtils";
    /**
     * 网络访问使用的client
     */
    public static HttpClient httpClient;

    /**
     * 连接超时时间
     */
    public static int CONNECT_TIMEOUT = 7000;

    /**
     * 数据读取超时时间
     */
    public static int READ_DAT_TIME_OUT = 12000;

    public static String getStrFromPath(String httpPath) throws SocketTimeoutException {
       return getStrFromPath(httpPath);
    }


    public static InputStream getInputStreamFromPath(String regex,String ... args) throws SocketTimeoutException {
        if(httpClient == null){
            httpClient = new DefaultHttpClient();
        }
        String path = String.format(regex,args);
        InputStream is = null;
        String res = null;
        HttpURLConnection httpConnection = null;
        try {
            URL tempUrl = new URL(path);
            httpConnection = (HttpURLConnection) tempUrl.openConnection();
            httpConnection.setReadTimeout(CONNECT_TIMEOUT);
            httpConnection.setConnectTimeout(READ_DAT_TIME_OUT);
            httpConnection.setDoOutput(false);
            httpConnection.setRequestMethod("GET");

            is = httpConnection.getInputStream();
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (ConnectException e) {
            throw new SocketTimeoutException();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
            if(httpConnection != null){
                httpConnection.disconnect();
            }
        }
        return is;
    }

    /**
     * 下载弹幕文件缓存到本地
     * @param chatId
     * @return
     */
    public static boolean downLoadDanmuFile(int chatId) throws SocketTimeoutException {
        String danmuFileUrl = String.format(Constant.GET_CHAT_FILE_SERVER_URL,chatId);
        String DanmuSaveFilePath = Constant.DANMU_FILE_SAVE_PATH+chatId;
        L.e("访问URL"+danmuFileUrl);
        File danmuFileSet = new File(Constant.DANMU_FILE_SAVE_PATH);
        if(!danmuFileSet.exists()){
            danmuFileSet.mkdirs();
        }
        File danmuFile = new File(DanmuSaveFilePath);
        L.e(danmuFile.getPath());
        if(!danmuFile.exists()){
            try {
                danmuFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        HttpURLConnection httpConnection = null;
        DeflateInputStream deflaterInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL tempUrl = new URL(danmuFileUrl);
            httpConnection = (HttpURLConnection) tempUrl.openConnection();
            httpConnection.setReadTimeout(CONNECT_TIMEOUT);
            httpConnection.setConnectTimeout(READ_DAT_TIME_OUT);
            httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36 LBBROWSER");
            httpConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            httpConnection.setRequestProperty("Accept-Encoding", "gzip");
            deflaterInputStream = new org.apache.http.client.entity.DeflateInputStream(httpConnection.getInputStream());
            fileOutputStream = new FileOutputStream(danmuFile);

            byte [] buffer = new byte[512];
            int length = 0;
            int totalLength = 0;
            while((length = deflaterInputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer,0,length);
                totalLength+= length;
            }
            fileOutputStream.flush();
        }catch (SocketTimeoutException e) {
            throw e;
        } catch (ConnectException e) {
            throw new SocketTimeoutException();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            IOUtils.closeQuietly(fileOutputStream);
            IOUtils.closeQuietly(deflaterInputStream);
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return true;
    }
}
