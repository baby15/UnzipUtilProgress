package unzip.test.com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import unzip.test.com.utils.L;

public class AnimWareView extends View {
    private Path mPath;
    private Paint mPaint;
    private int mItemWavwLength = 1200;
    private int dx;
    public AnimWareView(Context context) {
        this(context, null,0);
    }

    public AnimWareView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public AnimWareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
     //   mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        int originY = 300;
        int halfWaveLen = mItemWavwLength / 2;
        mPath.moveTo(-mItemWavwLength,originY);
        for (int i = -mItemWavwLength;i <= getWidth()+ mItemWavwLength;i+=mItemWavwLength) {
            L.e("执行i:" + i);
            mPath.quadTo(halfWaveLen / 2,-100,halfWaveLen,0);
            mPath.quadTo(halfWaveLen / 2,100,halfWaveLen,0);

        }
        canvas.drawPath(mPath,mPaint);
    }
}
