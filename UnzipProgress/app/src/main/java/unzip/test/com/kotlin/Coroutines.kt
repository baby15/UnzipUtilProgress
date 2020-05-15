package unzip.test.com.kotlin

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Coroutines {
    companion object {
        fun coroutines() {
            Log.d("AA", "协程初始化开始，时间: " + System.currentTimeMillis())

            GlobalScope.launch(Dispatchers.Unconfined) {
                Log.d("AA", "协程初始化完成，时间: " + System.currentTimeMillis())
                for (i in 1..3) {
                    Log.d("AA", "协程任务1打印第$i 次，时间: " + System.currentTimeMillis())
                }
                delay(500)
                for (i in 1..3) {
                    Log.d("AA", "协程任务2打印第$i 次，时间: " + System.currentTimeMillis())
                }
            }

            Log.d("AA", "主线程 sleep ，时间: " + System.currentTimeMillis())
           Thread.sleep(1000)
            Log.d("AA", "主线程运行，时间: " + System.currentTimeMillis())

            for (i in 1..3) {
                Log.d("AA", "主线程打印第$i 次，时间: " + System.currentTimeMillis())
            }
        }
        fun get(){

        }
    }

}