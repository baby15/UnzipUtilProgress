package unzip.test.com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DottedLineView extends View {
    public DottedLineView(Context context) {
        this(context,null,0);
    }

    public DottedLineView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DottedLineView(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);

        Path path = new Path();
        path.moveTo(100, 300);
//        path.lineTo(200, 100);
//        path.lineTo(1000, 600);
        path.quadTo(200, 200,300, 300);
        path.quadTo(400, 400,500, 300);
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},15);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}
