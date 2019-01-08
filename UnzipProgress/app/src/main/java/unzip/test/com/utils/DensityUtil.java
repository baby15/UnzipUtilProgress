package unzip.test.com.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;


public class DensityUtil {
	private static float scale;

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		if (scale == 0)
			scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		if (scale == 0)
			scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕密度   sw320和sw360公式计算
	 * 系统自动识别手机的最小宽度(smallestWidth宽度 * 160  除以   densityDpi)得到的值做宽高最小适配计算,
	 *    px = density * dp;density = densityDpi / 160;px = dp * (densityDpi / 160);
	 */
	public static void getPixelDisplayMetricsII(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;

		float density = dm.density;//只有五种情况 : 0.75/ 1.0/ 1.5/ 2.0/ 3.0
		int densityDpi = dm.densityDpi;//只有五种情况 : 120/ 160/ 240/ 320/ 480


		float xdpi = dm.xdpi;//水平方向上的准确密度, 即每英寸的像素点
		float ydpi = dm.ydpi;//垂直方向上的准确密度, 即每英寸的像素点
		L.e("宽:" + screenWidth + ", 高:" + screenHeight);
		L.e("密度 density:" + density + ",densityDpi:" + densityDpi);
		L.e("精确密度 xdpi:" + xdpi + ", ydpi:" + ydpi);
	}
	public static float getScreenDensity(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;//只有五种情况 : 0.75/ 1.0/ 1.5/ 2.0/ 3.0
		return density;
	}
}
