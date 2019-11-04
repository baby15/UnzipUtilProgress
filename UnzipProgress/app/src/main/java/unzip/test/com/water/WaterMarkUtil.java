package unzip.test.com.water;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import unzip.test.com.unzip.R;


/**
 * 描述：水印工具
 */
public class WaterMarkUtil {


    /**
     * 显示水印布局
     *
     * @param activity
     */
    public static boolean showWatermarkView(final Activity activity) {
        final ViewGroup rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        View framView = LayoutInflater.from(activity).inflate(R.layout.layout_watermark, null);
        //可对水印布局进行初始化操作
        rootView.addView(framView);
        return true;
    }
}

