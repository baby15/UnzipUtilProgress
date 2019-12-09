package httc.test.com.getphone.adapter

import android.content.Context
import httc.test.com.getphone.R
import httc.test.com.getphone.base.ListBaseAdapter
import httc.test.com.getphone.base.SuperViewHolder
import httc.test.com.getphone.bean.MyFile

class RvAdapter(context: Context?) : ListBaseAdapter<MyFile>(context) {
    override fun getLayoutId(): Int {
        return R.layout.item_demo
    }

    override fun onBindItemHolder(holder: SuperViewHolder?, position: Int) {

    }
}