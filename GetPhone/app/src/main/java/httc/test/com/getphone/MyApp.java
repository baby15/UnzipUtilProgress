package httc.test.com.getphone;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MyApp extends Application {
    private static MyApp instance;
    /**
     * sp存储
     */
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;




    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDate();
    }

    private void initDate() {



        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();

    }
    public String getApkDir() {

//        String apkDir = Environment.getDataDirectory() + "/" + this.getString(R.string.download_url);

        String apkDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/";
        L.e("apkDir---:" + apkDir);
        return apkDir;
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static MyApp getInstance() {
        return instance;
    }





}
