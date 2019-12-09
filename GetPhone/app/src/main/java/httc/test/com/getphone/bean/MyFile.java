package httc.test.com.getphone.bean;

import android.graphics.Bitmap;

/**
 * Created by mac on 2017/5/16.
 */

public class MyFile {
    private Long id;
    private Bitmap apk_icon;
    private String appName;
    private String packageName;
    private String versionName;
    private int versionCode;
    private int installed;
    private String filePath;
    private String fileSize;
    private String fileDate;
    public boolean flag;
    public String fileName;
    public MyFile() {
    }

    public MyFile(Long id, Bitmap apk_icon, String appName, String packageName, String versionName, int versionCode, int installed,
                  String filePath, String fileSize, String fileDate) {
        this.id  = id;
        this.apk_icon = apk_icon;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.installed = installed;
        this.fileDate = fileDate;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.appName = appName;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap getApk_icon() {
        return apk_icon;
    }

    public void setApk_icon(Bitmap apk_icon) {
        this.apk_icon = apk_icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getInstalled() {
        return installed;
    }

    public void setInstalled(int installed) {
        this.installed = installed;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

}
