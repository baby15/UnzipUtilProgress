package httc.test.com.getphone.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import httc.test.com.getphone.R;

public class BottomCircleLayout extends LinearLayout {
    private int itemDefaultBgResId = R.drawable.normal_circle;//单个元素默认背景样式
    private int itemSelectedBgResId = R.drawable.select_circle;//单个元素选中背景样式
    private int currentPosition;//当前选中位置
    private int itemHeight =18;//item宽高
    private int itemMargin = 10;//item间距
    private int item_space = 4;//item大小差距

    public BottomCircleLayout(Context context) {
        this(context, null, 0);
    }

    public BottomCircleLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomCircleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }
    public void initViews(int count) {
        removeAllViews();
        if(count <= 1 || itemHeight == 0){
            return;
        }
        View view = createView(itemHeight);
        view.setBackgroundResource(itemSelectedBgResId);
        addView(view);
       /* if(count == 1){
            return;
        }*/
        for (int i = 1; i < count; i++) {
            view = createView(itemHeight-item_space);
            view.setBackgroundResource(itemDefaultBgResId);
            addView(view);
        }
    }
    /**
     * 创建view
     * @param sideLength 边长
     * @return
     */
    public View createView(int sideLength){
        TextView textview = new TextView(getContext());
        setTextViewMargin(textview,sideLength);
        return textview;
    }
    //切换到目标位置
    public void changePosition(int position) {
        if(getChildCount() <= 1){
            return;
        }
        getChildAt(currentPosition).setBackgroundResource(itemDefaultBgResId);
         setTextViewMargin(getChildAt(currentPosition),itemHeight-item_space);
        currentPosition = position % getChildCount();
        getChildAt(currentPosition).setBackgroundResource(itemSelectedBgResId);
        setTextViewMargin(getChildAt(currentPosition),itemHeight);
    }

    private void setTextViewMargin(View view,int sideLength) {
        LinearLayout.LayoutParams params = new LayoutParams(sideLength, sideLength);
        if(itemMargin > 0){
            params.setMargins(itemMargin,0,itemMargin,0);
        }
        view.setLayoutParams(params);
    }

}
