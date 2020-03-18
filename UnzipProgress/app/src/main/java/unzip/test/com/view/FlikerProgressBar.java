package unzip.test.com.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import unzip.test.com.unzip.R;
import unzip.test.com.utils.L;


/**
 * Created by chenliu on 2016/8/26.<br/>
 * 描述：添加圆角支持 on 2016/11/11
 * </br>
 */
public class FlikerProgressBar extends View {
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    private int DEFAULT_HEIGHT_DP = 35;

    private int borderWidth;

    private float maxProgress = 100f;

    private Paint textPaint;

    private Paint bgPaint;

    private Paint pgPaint;

    private String progressText;

    private Rect textRect;

    private RectF bgRectf;

    /**
     * 左右来回移动的滑块
     */
   // private Bitmap flikerBitmap;

    /**
     * 滑块移动最左边位置，作用是控制移动
     */
   // private float flickerLeft;

    /**
     * 进度条 bitmap ，包含滑块
     */
    private Bitmap pgBitmap;

    private Canvas pgCanvas;

    /**
     * 当前进度
     */
    private float progress;

    private boolean isFinish;

    private boolean isStop;

    /**
     * 下载中颜色
     */
    private int loadingColor;

    /**
     * 暂停时颜色
     */
    private int stopColor;

    /**
     * 进度文本、边框、进度条颜色
     */
    private int progressColor;

    private int textSize;

    private int radius;

    //private Thread thread;

    BitmapShader bitmapShader;

    public FlikerProgressBar(Context context) {
        this(context, null, 0);
    }

    public FlikerProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlikerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FlikerProgressBar);
        try {
            textSize = (int) ta.getDimension(R.styleable.FlikerProgressBar_textSize, 12);
            loadingColor = ta.getColor(R.styleable.FlikerProgressBar_loadingColor, Color.parseColor("#40c4ff"));
            stopColor = ta.getColor(R.styleable.FlikerProgressBar_stopColor, Color.parseColor("#ff9800"));
            radius = (int) ta.getDimension(R.styleable.FlikerProgressBar_radius, 0);
            borderWidth = (int) ta.getDimension(R.styleable.FlikerProgressBar_borderWidth, 1);
        } finally {
            ta.recycle();
        }
    }

    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(borderWidth);

        pgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pgPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);

        textRect = new Rect();
        bgRectf = new RectF(borderWidth, borderWidth, getMeasuredWidth() - borderWidth, getMeasuredHeight() - borderWidth);

        if(isStop){
            progressColor = stopColor;
        } else{
            progressColor = loadingColor;
        }

       // flikerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flicker);
       // flickerLeft = -flikerBitmap.getWidth();

        initPgBimap();
    }

    private void initPgBimap() {
        pgBitmap = Bitmap.createBitmap(getMeasuredWidth() - borderWidth, getMeasuredHeight() - borderWidth, Bitmap.Config.ARGB_8888);
        pgCanvas = new Canvas(pgBitmap);
//        thread = new Thread(this);
//        thread.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        switch (heightSpecMode){
            case MeasureSpec.AT_MOST:
                height = dp2px(DEFAULT_HEIGHT_DP);
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                height = heightSpecSize;
                break;
        }
        setMeasuredDimension(widthSpecSize, height);

        if(pgBitmap == null){
            init();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //边框背景
        drawBackGround(canvas);

        //进度
        drawProgress(canvas);

         //进度text
        drawProgressText(canvas);

        //变色处理
        drawColorProgressText(canvas);
    }

    /**
     * 边框
     * @param canvas
     */
    private void drawBackGround(Canvas canvas) {
        bgPaint.setColor(progressColor);
        //left、top、right、bottom不要贴着控件边，否则border只有一半绘制在控件内,导致圆角处线条显粗
        canvas.drawRoundRect(bgRectf, radius, radius, bgPaint);
    }

    /**
     * 进度
     */
    @SuppressLint("WrongConstant")
    private void drawProgress(Canvas canvas) {
        pgPaint.setColor(progressColor);

        float right = (progress / maxProgress) * getMeasuredWidth();
        // 这里可以初始化画布
        pgCanvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        pgCanvas.drawColor(Color.parseColor("#F5F1F1"));

        //todo 不存在
       // pgCanvas.save(Canvas.CLIP_SAVE_FLAG);
        //画进度条
        //此为裁剪,裁剪出需要变换进度条的图像区域,区域裁剪完然后绘制颜色
        pgCanvas.clipRect(0, 0, right, getMeasuredHeight());
        pgCanvas.drawColor(progressColor);
        // TODO: 2018/9/19 restore恢复之前保存的状态,保存的复用条目的状态,产生影响.right到新复用条目为0,
        // todo 所以right并没有重新绘制新条目的颜色,如果重新恢复还是之前旧条目的颜色,所以需要重新初始化画布的颜色,
        // todo 但是出现的初始化时获取绘制宽度为0的问题
       pgCanvas.restore();

        if(!isStop){
            pgPaint.setXfermode(xfermode);
          //  pgCanvas.drawBitmap(flikerBitmap, flickerLeft, 0, pgPaint);
            pgPaint.setXfermode(null);
        }

        //控制显示区域
        bitmapShader = new BitmapShader(pgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        pgPaint.setShader(bitmapShader);
        canvas.drawRoundRect(bgRectf, radius, radius, pgPaint);
    }

    /**
     * 进度提示文本
     * @param canvas
     */
    private void drawProgressText(Canvas canvas) {
        textPaint.setColor(progressColor);
        progressText = getProgressText();
        textPaint.getTextBounds(progressText, 0, progressText.length(), textRect);
        int tWidth = textRect.width();
        int tHeight = textRect.height();
        float xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        float yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        canvas.drawText(progressText, xCoordinate, yCoordinate, textPaint);
    }

    /**
     * 变色处理
     * @param canvas
     */
    @SuppressLint("WrongConstant")
    private void drawColorProgressText(Canvas canvas) {
        textPaint.setColor(Color.WHITE);
        int tWidth = textRect.width();
        int tHeight = textRect.height();
        float xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        float yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        float progressWidth = (progress / maxProgress) * getMeasuredWidth();
        if(progressWidth > xCoordinate){
            //todo 不存在
           // canvas.save(Canvas.CLIP_SAVE_FLAG);
            float right = Math.min(progressWidth, xCoordinate + tWidth * 1.1f);
            canvas.clipRect(xCoordinate, 0, right, getMeasuredHeight());
            canvas.drawText(progressText, xCoordinate, yCoordinate, textPaint);
            canvas.restore();
        }
    }

    public void setProgress(float progress){//没有停止,绘制继续
        if(!isStop){//没有停止时更新进度
            if(progress < maxProgress){
                this.progress = progress;
            } else {
                finishLoad();
            }
            invalidate();
        }
    }

    public void setStop(boolean stop) {
        this.progress = 0;
        isStop = stop;
        if(isStop){
            progressColor = stopColor;
          //  thread.interrupt();
        } else {
            progressColor = loadingColor;
//            thread = new Thread(this);
//            thread.start();
        }
        invalidate();
    }

    public void finishLoad() {
        this.progress = maxProgress;
        isFinish = true;
        //setStop(true);
        isStop = true;
        if(isStop){
            progressColor = stopColor;
            //  thread.interrupt();
        } else {
            progressColor = loadingColor;
//            thread = new Thread(this);
//            thread.start();
        }
        invalidate();
    }

   /* public void toggle(){//暂停或者继续调用
        if(!isFinish){
            if(isStop){
                setStop(false);
            } else {
                setStop(true);
            }
        }
    }*/

    /*@Override
    public void run() {// 这段的含义  改变flickerLeft大小
        int width = flikerBitmap.getWidth();
        try {
            while (!isStop && !thread.isInterrupted()){
                flickerLeft += dp2px(5);
                float progressWidth = (progress / maxProgress) * getMeasuredWidth();//百分比占总宽度
                if(flickerLeft >= progressWidth){
                    flickerLeft = -width;
                }
                postInvalidate();
                Thread.sleep(20);
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 重置
     */
    /*public void reset(){
        setStop(true);
        progress = 0;
        isFinish = false;
        isStop = false;
        progressColor = loadingColor;
        progressText = "下载";
        //flickerLeft = -flikerBitmap.getWidth();
        initPgBimap();
    }*/
    /**
     * 重置
     */
    public void reset(){
        progress = 0;
        isFinish = false;
        isStop = false;
        progressColor = loadingColor;
        progressText = "下载";
        invalidate();

    }

    public float getProgress() {
        return progress;
    }

    public boolean isStop() {//暂停
        return isStop;
    }

    public boolean isFinish() {//完成
        return isFinish;
    }

    private String getProgressText() {
        String text= "";
        if(!isFinish){
            if(!isStop){
                if (progress == 0) {
                    text = "下载";
                } else {
                    text =  progress + "%";
                }

            } else {
                text = "继续";
            }
        } else{
            //text = "下载完成";
            text = "安装";
        }

        return text;
    }

    private int dp2px(int dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.e("触摸2");
        return super.onTouchEvent(event);
    }

}
