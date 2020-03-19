package unzip.test.com

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.httc.scan.jni.HttcSdk
import kotlinx.android.synthetic.main.activity_main.*

import unzip.test.com.unzip.R
import unzip.test.com.utils.L

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
      //  sample_text.text = stringFromJNI()
     //   HttcSdk.getFileMd5("")
        var md5 = HttcSdk.getFileMd5("E:/douyin.apk")
        sample_text.text = "没有值"
        md5?.let {
           sample_text.text = md5
       }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
