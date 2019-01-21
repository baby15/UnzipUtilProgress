package httc.test.com.getphone

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dm = DisplayMetrics()
       getWindowManager().getDefaultDisplay().getMetrics(dm)
        tv1.text = "densityDpi : " + dm.densityDpi + "---density : " + dm.density
        tv2.text = "计算适配的smallestWidth : " + dm.widthPixels / (dm.densityDpi / 160.0) + "dp"
        tv3.text = "宽 :" + dm.widthPixels + "  高:" + dm.heightPixels + "--屏幕尺寸:" + DensityUtil.getPingMuSize(this)
        tv4.text = "当前手机： " + SystemUtil.getDeviceBrand() + "  " + SystemUtil.getSystemModel() + " \n" + "当前系统： " +
                SystemUtil.getSystemVersion() + " "

    }
}
