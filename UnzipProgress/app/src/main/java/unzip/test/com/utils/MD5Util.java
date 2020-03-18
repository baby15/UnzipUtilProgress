/*
 * Copyright (c) 2015. fuwenbo
 */

package unzip.test.com.utils;

import com.httc.scan.jni.HttcSdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/**
 * md5加密
 *
 * @author huagnwenjun
 */
public class MD5Util {
    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断一个路径下是否存在相同的MD5值
     *
     * @param pathTotal 总路径
     * @param md5       比较的MD5值
     * @return true存在相同 false不存在相同
     */
    public static boolean isMd5Equ(String pathTotal, String md5) throws IOException {
        File file = new File(pathTotal);
        if (file.isFile()) {
            String fileMD5String = getFileMD5String(file);
            L.e("mad5path:" + file);
            L.e("mad5值:" + fileMD5String);
            if (md5.equalsIgnoreCase(fileMD5String)) {
                return true;
            }


        }
        return false;
    }

    public static String getFileMD5String(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(messagedigest.digest());
    }

    public static String getStringMD5(String str) {
        byte[] buffer = str.getBytes();
        messagedigest.update(buffer);
        return bufferToHex(messagedigest.digest());
    }

    public static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
    public static void main(String[] args) {

        try {
            System.out.println(HttcSdk.getFileMd5("E:/douyin.apk"));
            System.out.println(MD5Util.getFileMD5String(new File("E:/douyin.apk")));
        } catch (IOException e) {e.printStackTrace();

        }
    }
}
