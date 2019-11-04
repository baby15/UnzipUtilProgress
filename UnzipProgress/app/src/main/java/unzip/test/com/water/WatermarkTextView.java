package unzip.test.com.water;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;


public class WatermarkTextView extends android.support.v7.widget.AppCompatTextView {
    public WatermarkTextView(Context context) {
        super(context);
    }

    public WatermarkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(-15, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        super.onDraw(canvas);
    }
}