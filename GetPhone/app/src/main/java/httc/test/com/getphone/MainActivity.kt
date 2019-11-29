package httc.test.com.getphone

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.widget.Toast
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
                SystemUtil.getSystemVersion() + " " +  " \n" + "手机型号： " +
                SystemUtil.getSystemModel() + " "

        var start = "am start-activity -n com.android.camera2/com.android.camera.CameraActivity " +
                "--ez extra_turn_screen_on true -a android.media.action.VIDEO_CAMERA --ez android.intent.extra.USE_FRONT_CAMERA true"

        L.e("相机路径:" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
        button.setOnClickListener {
            var intent = Intent(this,CameraActivity::class.java)
            startActivity(intent)
          // startCamera()
        }

    }
    //录像
   /* adb shell am start-activity -n com.google.android.GoogleCamera/com.android.camera.CameraActivity
    --ez extra_turn_screen_on true
    -a android.media.action.VIDEO_CAMERA
    --ez android.intent.extra.USE_FRONT_CAMERA true*/

    //拍照
   /* adb shell am start-activity -n com.google.android.GoogleCamera/com.android.camera.CameraActivity
    --ez extra_turn_screen_on true
    -a android.media.action.STILL_IMAGE_CAMERA
    --ez android.intent.extra.USE_FRONT_CAMERA true
    --ei android.intent.extra.TIMER_DURATION_SECONDS 3*/
// /storage/emulated/0/DCIM/Camera/...jpg
    fun startCamera(){
        // 通过包名获取要跳转的app，创建intent对象
        var intent = getPackageManager().getLaunchIntentForPackage("com.google.android.GoogleCamera")
//        var intent = Intent()
        intent = Intent("com.android.camera.SecureCameraActivity")
// 这里如果intent为空，就说名没有安装要跳转的应用
        if (intent != null) {
//            intent.setAction("android.media.action.STILL_IMAGE_CAMERA")
            intent.setAction("android.media.action.STILL_IMAGE_CAMERA_SECURE")
// 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
           // intent.putExtra("extra_turn_screen_on", true)
            //intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
            intent.putExtra("android.intent.extra.USE_BACK_CAMERA", true)
            intent.putExtra("android.intent.extra.TIMER_DURATION_SECONDS", 3)
            startActivity(intent)
        } else {
// 没有安装要跳转的app应用，提醒一下
            Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show()
        }

    }
}
