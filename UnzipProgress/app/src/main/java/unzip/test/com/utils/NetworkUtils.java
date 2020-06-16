package unzip.test.com.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * 网络使用工具类
 */
public class NetworkUtils {
    /**
     * 根据域名得到ip地址
     * @param host
     * @return
     */
    public static String getInetAddress(String host) {
        String iPAddress = host;
        InetAddress returnStr1 = null;
        try {
            returnStr1 = java.net.InetAddress.getByName(host);
            iPAddress = returnStr1.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            L.e("hostError:" + e.toString());
            return iPAddress;
        }
        return iPAddress;
    }

    public static boolean isNetworkAvailable(Context context) {
        if(context !=null){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info !=null){
                return info.isAvailable();
            }
        }
        return false;
    }


    /**
     * 判断是不是wifi网络状态
     *
     * @param paramContext
     * @return
     */
    public static boolean isWifi(Context paramContext) {
        return "2".equals(getNetType(paramContext)[0]);
    }

    /**
     * 判断是不是2/3G网络状态
     *
     * @param paramContext
     * @return
     */
    public static boolean isMobile(Context paramContext) {
        return "1".equals(getNetType(paramContext)[0]);
    }

    /**
     * 网络是否可用
     *
     * @param paramContext
     * @return
     */
    public static boolean isNetAvailable(Context paramContext) {
        if ("1".equals(getNetType(paramContext)[0]) || "2".equals(getNetType(paramContext)[0])) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前网络状态 返回2代表wifi,1代表2G/3G
     *
     * @param paramContext
     * @return
     */
    public static String[] getNetType(Context paramContext) {
        String[] arrayOfString = {"Unknown", "Unknown"};
        PackageManager localPackageManager = paramContext.getPackageManager();
        if (localPackageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE", paramContext.getPackageName()) != 0) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }

        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (localConnectivityManager == null) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }

        NetworkInfo localNetworkInfo1 = localConnectivityManager.getNetworkInfo(1);
        if (localNetworkInfo1 != null && localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
            arrayOfString[0] = "2";
            return arrayOfString;
        }

        NetworkInfo localNetworkInfo2 = localConnectivityManager.getNetworkInfo(0);
        if (localNetworkInfo2 != null && localNetworkInfo2.getState() == NetworkInfo.State.CONNECTED) {
            arrayOfString[0] = "1";
            arrayOfString[1] = localNetworkInfo2.getSubtypeName();
            return arrayOfString;
        }

        return arrayOfString;
    }

    public static final boolean ping() {
        String result = null;
        try {

            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网

            Process p = Runtime.getRuntime().exec("ping -c 1 " + ip);// ping网址3次

            // 读取ping的内容，可以不加

            InputStream input = p.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(input));

            StringBuffer stringBuffer = new StringBuffer();

            String content = "";

            while ((content = in.readLine()) != null) {

                stringBuffer.append(content);

            }

            L.d("result content : " + stringBuffer.toString());

            // ping的状态

            int status = p.waitFor();

            if (status == 0) {

                result = "success";

                return true;

            } else {

                result = "failed";

            }

        } catch (IOException e) {

            result = "IOException";

        } catch (InterruptedException e) {

            result = "InterruptedException";

        } finally {

            L.d( "result = " + result);

        }

        return false;
    }


}