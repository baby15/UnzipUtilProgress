package httc.test.com.getphone;

/**
 * Created by huangwenjun
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.*;

/**
 * 获取手机上apk文件信息类，主要是判断是否安装再手机上了，安装的版本比较现有apk版本信息
 *
 * @author Dylan
 */
public class ApkSearchUtils {
    private static int INSTALLED = 0; // 表示已经安装，且跟现在这个apk文件是一个版本
    private static int UNINSTALLED = 1; // 表示未安装
    private static int INSTALLED_UPDATE = 2; // 表示已经安装，版本比本地版本要高，可以点击按钮更新
    private static int INSTALLED_LOW = -1; // 低版本

    private Context context;
    private Map<String, Integer> apkNameMap;//key,名称,value同key
    private Map<String, Integer> apkNameNewMap;//key,名称,value同key

    public List<MyFile> getFileList() {
        return myfileList;
    }

    private List<MyFile> myfileList;

    public Map<String, Integer> getApkNameMap() {
        return apkNameMap;
    }

    public void clearMap() {
        if (apkNameMap != null && apkNameMap.size() > 0) {
            apkNameMap.clear();
        }
    }

    public boolean isExistApkName(String filename) {
        if (apkNameMap != null && apkNameMap.size() > 0) {
            if (apkNameMap.containsKey(filename)) {
                return true;
            }
        }
        return false;
    }

    public ApkSearchUtils(Context context) {
        super();
        this.context = context;
    }

    public void clearFileList() {
        if (myfileList != null && myfileList.size() > 0) {
            myfileList.clear();
        }
    }

    public void getCameraPath() {
        File[] files = new File(MyApp.getInstance().getApkDir()).listFiles();//得到文件夹下的文件列表
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) {//照片
                    /**得到最后修改时间*/
                    long date = new Date(file.lastModified()).getTime();
                    if (DateUtils.getStringDate(date) == DateUtils.getStringDate(System.currentTimeMillis())) {
                        L.e("照片路径=" + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public List<MyFile> findAllAPKFile() {
        apkNameMap = new HashMap<>();
        myfileList = new ArrayList<>();
        try {
            File[] files = new File(MyApp.getInstance().getApkDir()).listFiles();//得到文件夹下的文件列表
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".apk")) {
                        MyFile myFileApk = analysisPackageApk(file);
                        if (myFileApk != null) {
                            myfileList.add(myFileApk);
                        }
                    }

                }
                updataMap();
            }

        } catch (Exception e) {
            e.printStackTrace();
            L.e("解析apk异常:" + e.getMessage());
        } finally {
        }
        return myfileList;
    }

    public MyFile analysisPackageApk(File file) {
        if (file.isFile() && file.getName().toLowerCase().endsWith(".apk")) {//是个文件
            MyFile myFile = new MyFile();
            myFile.fileName = file.getName();
            String apk_path = file.getAbsolutePath();// apk文件的绝对路径
            //  L.i("文件路径" + apk_path);
            PackageManager pm = MyApp.getInstance().getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path.trim(), PackageManager.GET_ACTIVITIES);
            ApplicationInfo appInfo = null;
            if (packageInfo != null) {
                /** 得到包名 ,appname*/
                String packageName = packageInfo.packageName;
                myFile.setPackageName(packageName);

                appInfo = packageInfo.applicationInfo;
                if (appInfo != null) {
                    /**获取apk的图标 */
                    appInfo.sourceDir = apk_path;
                    appInfo.publicSourceDir = apk_path;
                    Drawable apk_icon = appInfo.loadIcon(pm);
                    Bitmap bitmap = drawableToBitmap(apk_icon);
                    //BitmapDrawable bd = (BitmapDrawable) apk_icon;
                    // Bitmap bitmap = bd.getBitmap();
                    myFile.setApk_icon(bitmap);

                    String app_name = appInfo.loadLabel(pm).toString();
                    //   L.i(app_name);
                    myFile.setAppName(app_name);
                    /**得到大小*/
                    String dir = appInfo.publicSourceDir;
                    //    L.e("dir == " + dir);
                    /**转KB.MB.GB*/
                    // myFile.setFileSize(Utils.FormetFileSize(new File(dir).length()));
                    /**转得到时间*/
                    long date = new Date(new File(dir).lastModified()).getTime();
                    myFile.setFileDate(date + "");
                }


                /** apk的绝对路径*/
                myFile.setFilePath(file.getAbsolutePath());
                /** apk的版本名称 String */
                String versionName = packageInfo.versionName;
                myFile.setVersionName(versionName);
                /** apk的版本号码 int */
                int versionCode = packageInfo.versionCode;
                myFile.setVersionCode(versionCode);
                /**安装处理类型*/
                int type = doType(pm, packageName, versionCode);
                myFile.setInstalled(type);
/*
                if (isNew) {
                    apkNameMap.put(file.getName(), position++);
                } else {
                    apkNameMap.put(file.getName(), index);
                }*/

                return myFile;
                // fileList.add(myFile);//无论中高低版本都添加进去
                                /*if (INSTALLED_LOW != type) {//不是低版本
                                    if (map.containsKey(packageName)) {
                                        if (versionCode > map.get(packageName).getVersionCode()) {
                                            map.put(packageName,myFile);
                                        }
                                    } else {
                                        map.put(packageName,myFile);
                                    }

                                }*/

            }


        }
        return null;
    }

    public boolean isListChange() {//增,改,删
        boolean isChangeList = false;
        //  List<MyFile> fileList = new ArrayList<>();
        apkNameNewMap = new HashMap<>();
        long time1 = System.currentTimeMillis();
        File[] files = new File(MyApp.getInstance().getApkDir()).listFiles();//得到文件夹下的文件列表
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {//是个文件
                    String name_s = file.getName();
                    if (name_s.toLowerCase().endsWith(".apk")) {//重新遍历apk文件
                        if (!isExistApkName(name_s)) {//新文件,重新解析
                            MyFile myFile = analysisPackageApk(file);
                            if (myFile != null) {
                                myfileList.add(myFile);
                                L.e("增加apk");
                                isChangeList = true;
                            }
                        } else {//包括//旧文件
                            if (myfileList != null && myfileList.size() > 0) {
                                int index = apkNameMap.get(name_s);
                                if (!isFileEquals(myfileList.get(index))) {//对比时间日期,存在差异包,更新list
                                    MyFile myFileChange = analysisPackageApk(file);
                                    if (myFileChange != null) {
                                        myfileList.set(index, myFileChange);
                                    }

                                    L.e("apk有变动;" + name_s);
                                    isChangeList = true;
                                }

                            }
                        }
                        apkNameNewMap.put(name_s, -1);
                        //  L.e(name_s);
                        //查询数据库中是否存在此名字,不能存在重新解析包
                    }
                }
            }
        }
        deleteNoExistItem();
        if (isHaveDelete) {
            isChangeList = true;
        }
        if (isChangeList) {
            updataMap();
        }
        long time2 = System.currentTimeMillis();
        L.e("time_width=" + (time2 - time1));
        return isChangeList;

    }

    boolean isHaveDelete = false;

    private void deleteNoExistItem() {
        Iterator<MyFile> iterator1 = myfileList.iterator();
        while (iterator1.hasNext()) {
            MyFile itMyfile = iterator1.next();
            if (itMyfile != null) {
                if (!apkNameNewMap.containsKey(itMyfile.fileName)) {
                    iterator1.remove();
                    isHaveDelete = true;//有删除动作
                    L.e("apk被删除" + itMyfile.fileName);
                }
            }

        }
        L.e("myfileList size = " + myfileList.size());

  /*  //遍历旧map,再新map中不存在,则移除
        for(Iterator<Map.Entry<String, Integer>> iterator = apkNameMap.entrySet().iterator();iterator.hasNext();)

    {
        Map.Entry<String, Integer> entry = iterator.next();
        String key = entry.getKey();
        L.e("key= " + key + "---value = " + entry.getValue());
        if (!apkNameNewMap.containsKey(key)) {//不包含,说明已经被删除
            myfileList.remove((int) entry.getValue());//list移除,map存储的位置需要改变
            //    iterator.remove();
            isHaveDelete = true;//有删除动作
            L.e("apk被删除");
            L.e("myfileList size = " + myfileList.size());
        }
    }*/

    }

    public void updataMap() {//更新map
        clearMap();
        for (int i = 0; i < myfileList.size(); i++) {
            apkNameMap.put(myfileList.get(i).fileName, i);
        }
    }

    private boolean isFileEquals(MyFile myFile) {
        /**得到大小*/
        /**转KB.MB.GB*/
        /* String sizeFile = Utils.FormetFileSize(new File(myFile.getFilePath()).length());
         *//**转得到时间*//*
        long date = new Date(new File(myFile.getFilePath()).lastModified()).getTime();
        if (myFile.getFileSize().equals(sizeFile) && (myFile.getFileDate().equals(date + ""))) {
            return true;
        }*/

        return false;
    }
    /*
     * 判断该应用是否在手机上已经安装过，有以下集中情况出现
     * 1.未安装，这个时候按钮应该是“安装”点击按钮进行安装
     * 0.已安装，按钮显示“打开” 可以卸载该应用
     * 2.已安装，但是版本有更新，按钮显示“更新” 点击按钮就安装应用
     * -1,低版本
     */

    /**
     * 判断该应用在手机中的安装情况
     *
     * @param pm          PackageManager
     * @param packageName 要判断应用的包名
     * @param versionCode 要判断应用的版本号
     */
    public int doType(PackageManager pm, String packageName, int versionCode) {
        List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pi : pakageinfos) {
            String pi_packageName = pi.packageName;
            int pi_versionCode = pi.versionCode;
            //如果这个包名在系统已经安装过的应用中存在
            if (packageName.equals(pi_packageName)) {//已经安装
                //Log.i("test","此应用安装过了");
                if (versionCode == pi_versionCode) {
                    //    L.i("way", "打开");
                    return INSTALLED;
                } else if (versionCode > pi_versionCode) {
                    //   L.i("way", "有更新");
                    return INSTALLED_UPDATE;
                } else if (versionCode < pi_versionCode) {
                    //   L.i("way", "已经安装，低版本不显示");
                    return INSTALLED_LOW;
                }
            }
        }
        //   L.i("way", "未安装该应用，可以安装,包括低版本未安装的应用");
        return UNINSTALLED;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {

        // 获取 drawable 长宽
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0) {//针对返回-1的情况
            width = 60;
        }
        if (height <= 0) {//针对返回-1的情况
            height = 60;
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //获取本地第三方应用
    public List<MyFile> getLocalApp(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = activity.getPackageManager().queryIntentActivities(intent, 0);

        List<MyFile> localList = new ArrayList<>();

        for (int i = 0; i < resolveInfoList.size(); i++) {
            try {
                MyFile bean = new MyFile();
                ResolveInfo resolveInfo = resolveInfoList.get(i);

                bean.setAppName(resolveInfo.loadLabel(activity.getPackageManager()).toString());
                String packageName = resolveInfo.activityInfo.packageName;
                bean.setPackageName(packageName);

                Drawable icon = resolveInfo.loadIcon(activity.getPackageManager());
                //Crash in Android7.1.2
                //BitmapDrawable bd = (BitmapDrawable) icon;
                //Bitmap bitmap = bd.getBitmap();
                Bitmap bitmap = drawableToBitmap(icon);
                bean.setApk_icon(bitmap);

                PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(packageName, 0);

                if (packageInfo != null) {


                    bean.setVersionName(packageInfo.versionName);
                    bean.setVersionCode(packageInfo.versionCode);
                }

                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    //第三方应用
                    localList.add(bean);
                } else {
                    //系统应用
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return localList;
    }
}
