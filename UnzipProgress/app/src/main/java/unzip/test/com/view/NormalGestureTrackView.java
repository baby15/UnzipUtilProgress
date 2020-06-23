package unzip.test.com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;



public class NormalGestureTrackView extends View {
    private Path mPath = new Path();
    private Paint mPaint;
    public NormalGestureTrackView(Context context) {
        this(context,null,0);

    }


    public NormalGestureTrackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NormalGestureTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    private float mPrex,mPrey;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(),event.getY());
                mPrex = event.getX();
                mPrey = event.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float endx = (mPrex + event.getX()) / 2;
                float endy = (mPrey + event.getY()) / 2;
               // mPath.lineTo(event.getX(),event.getY());
                mPath.quadTo(mPrex,mPrey,endx,endy);
                mPrex = event.getX();
                mPrey = event.getY();
                invalidate();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mPath,mPaint);
    }
}
