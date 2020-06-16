package unzip.test.com.utils;

import android.text.TextUtils;

import static unzip.test.com.application.MyApp.editor;
import static unzip.test.com.application.MyApp.sp;


/**
 *sp工具类
 */

public class SPUtils {
    private final static String IP = "ip";
    private final static String PORT = "port";
    private final static String PORT_SFTP= "port_sftp";
    public static void setIpInfo(String ip, String port, String sftpPort) {
        editor.putString(IP,ip);
        editor.putString(PORT,port);
        editor.putString(PORT_SFTP,sftpPort);
        editor.commit();
    }
    public static String getSftpPort() {
        String sftp_port = sp.getString(PORT_SFTP,"");
        return sftp_port;
    }
    public static String getIp() {
      //  String ip = sp.getString(IP,"");
        return sp.getString(IP,"");
    }
    public static String getPort() {

        return sp.getString(PORT,"");
    }

    public static String getBaseUrlInfo() {
        String ip = sp.getString(IP, "");
        String port = sp.getString(PORT, "");
        //https://10.0.2.239:8082/httc/  分析端口
        return "https://" + ip + ":" + port + "/";
    }
    public static boolean isOpenSetIp() {
        String ip = sp.getString(IP, "");
        String port = sp.getString(PORT, "");
        String sftp_port = sp.getString(PORT_SFTP,"");
        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)|| TextUtils.isEmpty(sftp_port)) {
            return true;
        }
        return false;
    }
    public static void setBooleanInfo(String name, boolean isShow) {
        editor.putBoolean(name, isShow);
        editor.commit();
    }
    public static boolean getBooleanInfo(String name) {
        return   sp.getBoolean(name,false);
    }
    public static void setStringInfo(String name, String content) {
        editor.putString(name, content);
        editor.commit();
    }
    public static String getStringInfo(String name) {
      return   sp.getString(name,"");
    }



}
