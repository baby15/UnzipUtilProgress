package unzip.test.com.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static MyApp getInstance() {
        return instance;
    }





}
