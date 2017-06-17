package circlebar.oden.com.circleseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 项目名称：CircleSeekbarDemo
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/3/3 14:32
 */
public class CircleSeekbar extends View {
    private final String TAG = "CircleSeekbar";
    private static int INVALID_PROGRESS_VALUE = -1;

    private Paint circlePaint;
    private Paint progressPaint;
    private Paint paintDegree;
    private int circleColor;
    private int progressColor;
    private int textColor;
    private int textSize = 12;
    private int progressWidth = 6;
    private int circleWidth = 6;
    private int mProgress = 0;
    private int maxProgress = 100;
    private int padding = 5;
    private boolean mTouchInside = true;
    private String[] degreeText = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90"};

    private boolean enable = true;
    private boolean isOnlyScrollOneCircle = true;
    private Drawable mThumb;

    private int maxWidth = 6;
    private int radius;
    private int diameter;
    private int mWidth;
    private double mCurAngle = 0;
    private int centreX;
    private int centreY;
    private int mThumbXPos;
    private int mThumbYPos;
    private float mTouchIgnoreRadius;
    private boolean isFirstTouch = true;
    private RectF mCircleRect = new RectF();

    private OnSeekBarChangeListener onSeekBarChangeListener;

    public CircleSeekbar(Context context) {
        super(context);
        init(context, null);
    }

    public CircleSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float density = context.getResources().getDisplayMetrics().density;
        int thumbHalfheight = 0;
        int thumbHalfWidth = 0;

        circleColor = getResources().getColor(R.color.circleseekbar_gray);
        progressColor = getResources().getColor(R.color.circleseekbar_blue_light);
        textColor = getResources().getColor(R.color.circleseekbar_text_color);
        progressWidth = (int) (progressWidth * density);
        circleWidth = (int) (circleWidth * density);
        textSize = (int) (textSize * density);

        padding = (int) (padding * density);
        mThumb = getResources().getDrawable(R.drawable.seekbar_control_selector);

        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleSeekbar);
            circleColor = typedArray.getColor(R.styleable.CircleSeekbar_circleColor, circleColor);
            progressColor = typedArray.getColor(R.styleable.CircleSeekbar_circleColor, progressColor);
            textColor = typedArray.getColor(R.styleable.CircleSeekbar_textColor, textColor);
            textSize = typedArray.getColor(R.styleable.CircleSeekbar_textSize, textSize);
            circleWidth = (int) typedArray.getDimension(R.styleable.CircleSeekbar_circleWidth, circleWidth);
            progressWidth = (int) typedArray.getDimension(R.styleable.CircleSeekbar_progressWidth, progressWidth);
            mProgress = typedArray.getInteger(R.styleable.CircleSeekbar_progress, mProgress);
            maxProgress = typedArray.getInteger(R.styleable.CircleSeekbar_maxProgress, maxProgress);
            mTouchInside = typedArray.getBoolean(R.styleable.CircleSeekbar_touchInside, mTouchInside);
            Drawable thumb = typedArray.getDrawable(R.styleable.CircleSeekbar_thumb);
            if (thumb != null) {
                mThumb = thumb;
            }

            thumbHalfheight = (int) mThumb.getIntrinsicHeight() / 2;
            thumbHalfWidth = (int) mThumb.getIntrinsicWidth() / 2;
            mThumb.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth,
                    thumbHalfheight);
            typedArray.recycle();
        }

        padding = thumbHalfheight + padding;

        maxWidth = circleWidth > progressWidth ? circleWidth : progressWidth;
        mProgress = (mProgress > maxProgress) ? maxProgress : mProgress;
        mProgress = (mProgress < 0) ? 0 : mProgress;

        circlePaint = new Paint();
        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);

        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);

        paintDegree = new Paint();
        paintDegree.setColor(textColor);
        paintDegree.setAntiAlias(true);
        paintDegree.setStyle(Paint.Style.FILL);
        paintDegree.setStrokeWidth(1);
        paintDegree.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.RED);
        canvas.drawCircle(mWidth / 2, mWidth / 2, radius, circlePaint);
        canvas.drawArc(mCircleRect, -90, (float) mCurAngle, false, progressPaint);
        drawText(canvas);
        canvas.translate(mThumbXPos, mThumbYPos);
        mThumb.draw(canvas);
    }

    private void drawText(Canvas canvas) {
        float rotation = 360 / degreeText.length;
        for (int i = 0; i < degreeText.length; i++) {
            canvas.drawText(degreeText[i], centreX - paintDegree.measureText(degreeText[i]) / 2, 100/*字体的高度*/, paintDegree);
            canvas.rotate(rotation, centreX, centreY);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mWidth = Math.min(width, height);
        centreX = mWidth / 2;
        centreY = mWidth / 2;

        float left = getPaddingLeft() + maxWidth / 2 + padding;
        float top = getPaddingTop() + maxWidth / 2 + padding;
        float right = mWidth - getPaddingRight() - maxWidth / 2 - padding;
        float bottom = mWidth - getPaddingBottom() - maxWidth / 2 - padding;

        diameter = mWidth - getPaddingLeft() - getPaddingRight() - maxWidth - padding * 2;
        radius = diameter / 2;
        mCircleRect.set(left, top, left + diameter, top + diameter);

        updateThumbPosition(mCurAngle);

        setTouchInSide(mTouchInside);
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enable) {
            this.getParent().requestDisallowInterceptTouchEvent(true);//通知父控件勿拦截本次触摸事件

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    updateOnTouch(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateOnTouch(event);
                    break;
                case MotionEvent.ACTION_UP:
                    isFirstTouch = true;
                    setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isFirstTouch = true;
                    setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return true;
        }
        return false;
    }

    private void updateOnTouch(MotionEvent event) {
        boolean ignoreTouch = ignoreTouch(event.getX(), event.getY());
        if (ignoreTouch) {
            return;
        }
        setPressed(true);
        getTouchDegrees(event.getX(), event.getY());
        mProgress = getProgressForAngle(mCurAngle);
        updateProgress(mProgress, true);
    }

    //根据触摸点的坐标获取角度值
    private double getTouchDegrees(float xPos, float yPos) {
        float x = xPos - centreX;
        float y = yPos - centreY;

        // convert to arc Angle
        double angle = Math.toDegrees(Math.atan2(y, x) + (Math.PI / 2));

        if (angle < 0) {
            angle = 360 + angle;
        }

        if (isOnlyScrollOneCircle) {
            if (mCurAngle > 270 && angle < 90) {
                mCurAngle = 360;
            } else if (mCurAngle < 90 && angle > 270) {
                mCurAngle = 0;
            } else {
                mCurAngle = angle;
            }
        } else {
            mCurAngle = angle;
        }
        if (isFirstTouch) {  //如果是滑动前第一次点击则总是移动到该度数
            mCurAngle = angle;
            isFirstTouch = false;
        }

        Log.d(TAG, "getTouchDegrees: " + angle);
        return mCurAngle;
    }

    //根据触摸的角度值获取滑动条progree的值
    private int getProgressForAngle(double angle) {
        int touchProgress = (int) Math.round(maxProgress * angle / 360);

        touchProgress = (touchProgress < 0) ? INVALID_PROGRESS_VALUE
                : touchProgress;
        touchProgress = (touchProgress > maxProgress) ? INVALID_PROGRESS_VALUE
                : touchProgress;
        return touchProgress;
    }

    //更新view
    private void updateProgress(int progress, boolean fromUser) {
        if (progress == INVALID_PROGRESS_VALUE) {
            return;
        }

        progress = (progress > maxProgress) ? maxProgress : progress;
        progress = (progress < 0) ? 0 : progress;
        mProgress = progress;

        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onProgressChanged(this, progress, fromUser);
        }

        updateThumbPosition(mCurAngle);
        invalidate();
    }

    //更新thum的坐标
    private void updateThumbPosition(double angle) {
        double cos = -Math.cos(Math.toRadians(angle));

        if (angle < 180) {
            mThumbXPos = (int) (getMeasuredWidth() / 2 + Math.sqrt(1 - cos * cos) * radius);
        } else {
            mThumbXPos = (int) (getMeasuredWidth() / 2 - Math.sqrt(1 - cos * cos) * radius);
        }

        mThumbYPos = (int) (centreX + radius * (float) cos);
    }

    //设置触摸生效范围
    public void setTouchInSide(boolean isEnabled) {
        int thumbHalfheight = (int) mThumb.getIntrinsicHeight() / 2;
        int thumbHalfWidth = (int) mThumb.getIntrinsicWidth() / 2;
        mTouchInside = isEnabled;
        if (mTouchInside) {
            mTouchIgnoreRadius = (float) radius / 4;
        } else {
            // Don't use the exact radius makes interaction too tricky
            mTouchIgnoreRadius = radius - Math.min(thumbHalfWidth, thumbHalfheight);
        }
    }

    private boolean ignoreTouch(float xPos, float yPos) {
        boolean ignore = false;
        float x = xPos - centreX;
        float y = yPos - centreY;

        float touchRadius = (float) Math.sqrt(((x * x) + (y * y)));
        if (touchRadius < mTouchIgnoreRadius) {
            ignore = true;
        }
        return ignore;
    }

    //重写drawableStateChanged，同步改变thum的状态
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mThumb != null && mThumb.isStateful()) {
            int[] state = getDrawableState();
            mThumb.setState(state);
        }
        invalidate();
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(CircleSeekbar circleSeekbar, int progress, boolean fromUser);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
        invalidate();
    }

    public void setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
        invalidate();
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    public void setPadding(int padding) {
        this.padding = padding;
        invalidate();
    }

    public void setmTouchInside(boolean mTouchInside) {
        this.mTouchInside = mTouchInside;
    }

    public void setDegreeText(String[] degreeText) {
        this.degreeText = degreeText;
        invalidate();
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setOnlyScrollOneCircle(boolean onlyScrollOneCircle) {
        isOnlyScrollOneCircle = onlyScrollOneCircle;
    }

}
