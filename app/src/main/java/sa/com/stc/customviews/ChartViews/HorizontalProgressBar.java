package sa.com.stc.customviews.ChartViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import sa.com.stc.customviews.R;


public class HorizontalProgressBar extends View {

    private final int bubbleColor;
    private final int bubbleTextColor;
    private final float BubbleTextSize;
    private int bubbleTextHeight;
    private Rect bubbleTextBound = new Rect();
    private int barColor;
    private int textColor;
    private float textSize;
    private double maxValue;
    private double value;
    private String unit;
    private String maxOutputText;
    private String outputText;
    Paint fillPaint;
    Paint pinPaint;
    Paint bubbleTextPaint;
    Paint textPaint;

    float[] hsv = new float[3];


    int bubbleTextWidth;

    double progressValuePercentage = 50f;
    private long animationDuration = 1000;
    private float radiusValue = 35;
    private float mPadding = 8;
    private Rect textBound = new Rect();

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.HorizontalProgressBar);
    }

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }


    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar, defStyleAttr, 0);


        barColor = a.getColor(R.styleable.HorizontalProgressBar_HorizontalProgressBar_chart_color, Color.RED);
        bubbleColor = a.getColor(R.styleable.HorizontalProgressBar_HorizontalProgressBar_bubble_color, Color.RED);
        textColor = a.getColor(R.styleable.HorizontalProgressBar_HorizontalProgressBar_textColor, Color.BLACK);
        bubbleTextColor = a.getColor(R.styleable.HorizontalProgressBar_HorizontalProgressBar_bubbleTextColor, Color.BLACK);
        textSize = a.getDimension(R.styleable.HorizontalProgressBar_HorizontalProgressBar_textSize, getResources().getDimension(R.dimen.unit_text_size));
        BubbleTextSize = a.getDimension(R.styleable.HorizontalProgressBar_HorizontalProgressBar_bubbleTextSize, getResources().getDimension(R.dimen.unit_text_size));
        animationDuration = a.getInt(R.styleable.HorizontalProgressBar_HorizontalProgressBar_animation_duration, 1000);
        maxValue = a.getFloat(R.styleable.HorizontalProgressBar_HorizontalProgressBar_maxValue, 100f);
        value = a.getFloat(R.styleable.HorizontalProgressBar_HorizontalProgressBar_value, 50f);
        unit = a.getString(R.styleable.HorizontalProgressBar_HorizontalProgressBar_unit);

        progressValuePercentage = value * 100d / maxValue;

        a.recycle();

        fillPaint = new Paint();
        fillPaint.setColor(barColor);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);

        pinPaint = new Paint();
        pinPaint.setColor(bubbleColor);
        pinPaint.setStyle(Paint.Style.STROKE);
        pinPaint.setStrokeWidth(4f);
        pinPaint.setAntiAlias(true);

        bubbleTextPaint = new Paint();
        bubbleTextPaint.setColor(bubbleTextColor);
        bubbleTextPaint.setStyle(Paint.Style.FILL);
        bubbleTextPaint.setTextAlign(Paint.Align.CENTER);
        bubbleTextPaint.setTextSize(BubbleTextSize);
        bubbleTextPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        outputText = String.valueOf(value) + " " + (TextUtils.isEmpty(unit) ? "" : unit);
        maxOutputText = String.valueOf(maxValue) + " " + (TextUtils.isEmpty(unit) ? "" : unit);

        bubbleTextPaint.getTextBounds(outputText, 0, outputText.length(), bubbleTextBound);
        bubbleTextWidth = bubbleTextBound.width();
        bubbleTextHeight = bubbleTextBound.height();
        textPaint.getTextBounds(maxOutputText, 0, maxOutputText.length(), textBound);
        int textWidth = textBound.width();
        int textHeight = textBound.height();


        int progressWidth = (int) (getWidth() - textWidth - mPadding);

        double progressValue = (progressWidth * progressValuePercentage) / 100f;

        Bitmap bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(bmp);


        Paint paint = new Paint();
        Color.colorToHSV(barColor, hsv);

        hsv[1] = hsv[1] * 0.5f;
        hsv[2] = hsv[2] + hsv[2] * 0.5f;
        paint.setColor(Color.HSVToColor(hsv));

        bitmapCanvas.drawRoundRect(mPadding + bubbleTextWidth, mPadding, progressWidth - mPadding, (getHeight() / 3) - mPadding, radiusValue, radiusValue, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


        paint.setColor(barColor);

        float endProgress = (float) (progressValue + mPadding + bubbleTextWidth);

        if (endProgress > (getWidth() - 2 * mPadding) - (textWidth))
            endProgress = (getWidth() - 2 * mPadding) - (textWidth);

        bitmapCanvas.drawRoundRect(mPadding + bubbleTextWidth, mPadding, endProgress, (getHeight() / 3) - mPadding, 0, 0, paint);

        paint.setXfermode(null);
        canvas.drawBitmap(bmp, 0, 0, paint);

        canvas.save();

        canvas.translate((float) endProgress, 0);

        canvas.drawLine(-2, (getHeight() / 4), -2, (getHeight() / 2), pinPaint);


        canvas.translate(0, (getHeight() / 2));

        fillPaint.setColor(bubbleColor);

        canvas.drawRoundRect((-bubbleTextWidth / 3) * 2, -bubbleTextHeight, (bubbleTextWidth / 3) * 2, bubbleTextHeight, 35, 35, fillPaint);


        canvas.drawText(outputText, 0, (bubbleTextHeight - mPadding) / 2, bubbleTextPaint);

        canvas.restore();

        canvas.translate(progressWidth, 0);

        canvas.drawText(maxOutputText, 0, ((getHeight() / 3)) / 2 + mPadding, textPaint);

    }


    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, (float) progressValuePercentage);
        animator.setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressValuePercentage = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void setProgressValue(double progressValue) {
        this.value = progressValue;
        if (progressValue > maxValue)
            progressValue = maxValue;
        this.progressValuePercentage = progressValue * 100 / maxValue;
        setProgressValue(progressValue, animationDuration);
    }

    public void setProgressValue(double progressValue, long durationInMillies) {
        if (progressValue > maxValue)
            progressValue = maxValue;

        this.progressValuePercentage = progressValue * 100 / maxValue;

        this.animationDuration = durationInMillies;

        startAnimation();
    }

    public void setMax(double max) {
        this.maxValue = max;
        invalidate();
    }

    public void setProgressColor(int color) {
        this.barColor = color;
        invalidate();
    }
}
