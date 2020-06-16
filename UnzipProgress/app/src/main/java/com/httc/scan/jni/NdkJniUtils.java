package com.httc.scan.jni;

public class NdkJniUtils {
    public native String nativeGenerateKey(String name);

    static {
        System.loadLibrary("YanboberJniLibName");
    }
}
