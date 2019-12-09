package httc.test.com.getphone.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager

import httc.test.com.getphone.R
import httc.test.com.getphone.adapter.RvAdapter
import httc.test.com.getphone.bean.MyFile
import kotlinx.android.synthetic.main.activity_demo_recycler.*

import httc.test.com.getphone.utils.L
import httc.test.com.getphone.widget.PagerConfig
import httc.test.com.getphone.widget.PagerGridLayoutManager
import httc.test.com.getphone.widget.PagerGridSnapHelper


class DemoRecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_recycler)

        val rvAdapter = RvAdapter(this)

        // 1.水平分页布局管理器
        val layoutManager = PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL)
        recyclerview.setLayoutManager(layoutManager)

        // 2.设置滚动辅助工具
        val pageSnapHelper = PagerGridSnapHelper()
        pageSnapHelper.attachToRecyclerView(recyclerview)
        // 使用示例
        //PagerConfig.setFlingThreshold(10)

        // 设置滚动速度(滚动一英寸所耗费的微秒数，数值越大，速度越慢，默认为 60f)
        PagerConfig.setMillisecondsPreInch(200f)
        var list = arrayListOf<MyFile>()
        for (index in  1 ..20) {
            list.add(MyFile())
        }
        rvAdapter.setDataList(list)
        recyclerview.adapter = rvAdapter
        bottom_circle.initViews(layoutManager.totalPageCount)
        layoutManager.setPageListener(object : PagerGridLayoutManager.PageListener {
            override fun onPageSelect(pageIndex: Int) {
                L.e("onPageSelect=" + pageIndex)
               bottom_circle.changePosition(pageIndex)
            }

            override fun onPageSizeChanged(pageSize: Int) {
                L.e("onPageSizeChanged=" + pageSize)


            }

        })
    }


}