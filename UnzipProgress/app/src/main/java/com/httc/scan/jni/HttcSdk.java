package com.httc.scan.jni;

public class HttcSdk {
    static {
        System.loadLibrary("hash");
    }
   public static native String getFileMd5(String filePath);//获取文件的md5值,c计算,效率大于java

}
