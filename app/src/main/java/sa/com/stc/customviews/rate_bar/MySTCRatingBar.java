package sa.com.stc.customviews.rate_bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import sa.com.stc.customviews.R;

public class MySTCRatingBar extends View {

    private static final int NO_RATING = 0;
    private static int MAX_RATE = 5;
    private boolean isSliding;
    private float slidePosition;
    private PointF[] points;
    private float itemWidth;
    private float defaultRateTextSize;
    private float secondaryRateTextSize;

    private Drawable rateSecondaryDrawable;
    private Drawable rateDefaultDrawable;
    private OnRatingSliderChangeListener listener;
    private int currentRating = NO_RATING;
    private int rateDrawableWidth, rateDrawableHeight;
    private int horizontalSpacing;
    private boolean isEnabled;
    private boolean drawText;
    private int rating = NO_RATING;
    private TextPaint rateSecondaryTextPaint;
    private TextPaint rateDefaultTextPaint;
    List<Integer> rateDefaultDrawableList;
    List<Integer> rateSecondaryDrawableList;

    public MySTCRatingBar(Context context) {
        this(context, null);
    }

    public MySTCRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySTCRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        isSliding = false;

        rateDefaultTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        rateSecondaryTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RateBar, 0, 0);
            try {
                rateDrawableWidth = ta.getDimensionPixelSize(R.styleable.RateBar_rateDrawableWidth, 0);
                rateDrawableHeight = ta.getDimensionPixelSize(R.styleable.RateBar_rateDrawableHeight, 0);
                horizontalSpacing = ta.getDimensionPixelSize(R.styleable.RateBar_horizontalSpacing, 0);
                isEnabled = ta.getBoolean(R.styleable.RateBar_enabled, true);
                drawText = ta.getBoolean(R.styleable.RateBar_drawText, true);
                rating = Math.min(ta.getInt(R.styleable.RateBar_rating, NO_RATING), MAX_RATE);
                int resDefault = ta.getResourceId(R.styleable.RateBar_rateDefaultDrawable, R.drawable.rate_app_empty_circle);
                int resSecondary = ta.getResourceId(R.styleable.RateBar_rateSecondaryDrawable, R.drawable.rate_app_filled_circle);
                rateDefaultDrawable = ResourcesCompat.getDrawable(getResources(), resDefault, getContext().getTheme());
                rateSecondaryDrawable = ResourcesCompat.getDrawable(getResources(), resSecondary, getContext().getTheme());

                if (rateDrawableWidth == 0)
                    rateDrawableWidth = rateSecondaryDrawable.getIntrinsicWidth();

                if (rateDrawableHeight == 0)
                    rateDrawableHeight = rateSecondaryDrawable.getIntrinsicHeight();

                int defRateTextColor = ta.getColor(R.styleable.RateBar_rateDefaultTextColor, ContextCompat.getColor(getContext(), R.color.rateDefaultTextColor));
                int secondaryRateTextColor = ta.getColor(R.styleable.RateBar_rateSecondaryTextColor, ContextCompat.getColor(getContext(), R.color.rateSecondaryTextColor));

                defaultRateTextSize = ta.getDimensionPixelSize(R.styleable.RateBar_rateDefaultTextSize, Math.min(rateDrawableWidth, rateDrawableHeight) / 4);
                rateDefaultTextPaint.setTextSize(defaultRateTextSize);
                rateDefaultTextPaint.setColor(defRateTextColor);

                secondaryRateTextSize = ta.getDimensionPixelSize(R.styleable.RateBar_rateSecondaryTextSize, Math.min(rateDrawableWidth, rateDrawableHeight) / 4);
                rateSecondaryTextPaint.setTextSize(secondaryRateTextSize);
                rateSecondaryTextPaint.setColor(secondaryRateTextColor);

            } finally {
                ta.recycle();
            }
        }

        points = new PointF[MAX_RATE];
        for (int i = 0; i < MAX_RATE; i++) {
            points[i] = new PointF();
        }
        if (rating != NO_RATING)
            setRating(rating);
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        super.setEnabled(enabled);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt("ratingCount", rating);
        bundle.putInt("currentRating", currentRating);
        bundle.putBoolean("isSliding", isSliding);
        bundle.putFloat("slidePosition", slidePosition);
        Parcelable parcelable = super.onSaveInstanceState();
        bundle.putParcelable("main", parcelable);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = ((Bundle) state);
            rating = bundle.getInt("ratingCount", 0);
            currentRating = bundle.getInt("currentRating", NO_RATING);
            isSliding = bundle.getBoolean("isSliding", false);
            slidePosition = bundle.getFloat("slidePosition", 0f);
            if (listener != null)
                listener.onPendingRating(rating);
            super.onRestoreInstanceState(((Bundle) state).getParcelable("main"));
        } else
            super.onRestoreInstanceState(state);
    }

    /**
     * Set a listener that will be invoked whenever the users interacts with the AppRateBar.
     *
     * @param listener Listener to set.
     */
    public void setOnRatingSliderChangeListener(OnRatingSliderChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            // Disable all input if the slider is disabled
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                isSliding = true;
                slidePosition = getLayoutDirection() == LAYOUT_DIRECTION_RTL ?
                        MAX_RATE - getRelativePosition(event.getX()) :
                        getRelativePosition(event.getX());
                rating = (int) Math.ceil(slidePosition);
                if (listener != null && rating != currentRating) {
                    currentRating = rating;
                    listener.onPendingRating(rating);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                currentRating = NO_RATING;
                if (listener != null)
                    listener.onFinalRating((int) Math.ceil(slidePosition));
                rating = (int) Math.ceil(slidePosition);
                break;
            case MotionEvent.ACTION_CANCEL:
                currentRating = NO_RATING;
                if (listener != null)
                    listener.onCancelRating();
                isSliding = false;
                break;
            default:
                break;
        }

        invalidate();
        return true;
    }

    private float getRelativePosition(float x) {
        float position = x / itemWidth;
        position = Math.max(position, 0);
        return Math.min(position, MAX_RATE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        itemWidth = w / (float) MAX_RATE;
        updatePositions();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = rateDrawableWidth * MAX_RATE + horizontalSpacing * (MAX_RATE - 1) +
                getPaddingLeft() + getPaddingRight();
        int height = rateDrawableHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < MAX_RATE; i++) {
            PointF pos = points[i];
            canvas.save();
            canvas.translate(pos.x, pos.y);
            drawRateDrawable(canvas, i);
            canvas.restore();
        }
    }

    private void drawRateDrawable(Canvas canvas, int position) {

        // Draw the rated once
        if (isSliding && position <= slidePosition) {
            int rating = (int) Math.ceil(slidePosition);
            if (rating > 0) {
                drawRateDrawable(canvas, rateDefaultDrawableList != null ? ContextCompat.getDrawable(getContext(), rateDefaultDrawableList.get(position)) : rateDefaultDrawable, position + 1, rateDefaultTextPaint);
                if (rating - 1 == position)
                    drawRateDrawable(canvas, rateSecondaryDrawableList != null ? ContextCompat.getDrawable(getContext(), rateSecondaryDrawableList.get(position)) : rateSecondaryDrawable, position + 1, rateSecondaryTextPaint);
            } else
                drawRateDrawable(canvas, rateDefaultDrawableList != null ? ContextCompat.getDrawable(getContext(), rateDefaultDrawableList.get(position)) : rateDefaultDrawable, position + 1, rateDefaultTextPaint);
        } else {
            // Draw the default rate
            drawRateDrawable(canvas, rateDefaultDrawableList != null ? ContextCompat.getDrawable(getContext(), rateDefaultDrawableList.get(position)) : rateDefaultDrawable, position + 1, rateDefaultTextPaint);
        }
    }

    private void drawRateDrawable(Canvas canvas, Drawable rateDrawable, int text, TextPaint textPaint) {
        canvas.save();
        canvas.translate(-rateDrawableWidth / 2, -rateDrawableHeight / 2);
        rateDrawable.setBounds(0, 0, rateDrawableWidth, rateDrawableHeight);
        rateDrawable.draw(canvas);

        if (drawText) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float x = rateDrawableWidth / 2 - getTextWidth(textPaint, String.valueOf(text)) / 2;
            float baseline = rateDrawableHeight / 2 - (fontMetrics.bottom + fontMetrics.top) / 2;

            canvas.drawText(String.valueOf(text), x, baseline, textPaint);
        }
        canvas.restore();
    }

    private float getTextWidth(TextPaint textPaint, String text) {
        return textPaint.measureText(text);
    }


    private void updatePositions() {
        boolean isRTL = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        float left = isRTL ? getWidth() : 0;
        for (int i = 0; i < MAX_RATE; i++) {
            float posY = getHeight() / 2;
            float posX = isRTL ? left - rateDrawableWidth / 2 : left + rateDrawableWidth / 2;

            left = left + (isRTL ? -rateDrawableWidth : rateDrawableWidth);
            if (i > 0) {
                posX = posX + (isRTL ? -horizontalSpacing : horizontalSpacing);
                left = left + (isRTL ? -horizontalSpacing : horizontalSpacing);
            } else {
                posX = posX + (isRTL ? -getPaddingRight() : getPaddingRight());
                left = left + (isRTL ? -getPaddingRight() : getPaddingRight());
            }

            points[i].set(posX, posY);

        }
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 0 || rating > MAX_RATE)
            throw new IndexOutOfBoundsException("Rating must be between 0 and " + MAX_RATE);

        this.rating = rating;
        slidePosition = (float) (rating - 0.1);
        isSliding = true;
        invalidate();
        if (listener != null)
            listener.onFinalRating(rating);
    }

    public void setRateDefaultDrawableList(List<Integer> rateDefaultDrawableList) {
        this.rateDefaultDrawableList = rateDefaultDrawableList;
        MAX_RATE = rateDefaultDrawableList != null ? rateDefaultDrawableList.size() : MAX_RATE;
    }

    public void setRateSecondaryDrawableList(List<Integer> rateSecondaryDrawableList) {
        this.rateSecondaryDrawableList = rateSecondaryDrawableList;
        MAX_RATE = rateDefaultDrawableList != null ? rateDefaultDrawableList.size() : MAX_RATE;
    }

    /**
     * A callback that notifies clients when the user starts rating, changes the rating
     * value and when the rating has ended.
     */
    public interface OnRatingSliderChangeListener {

        /**
         * Notification that the user has moved over to a different rating value.
         * The rating value is only temporary and might change again before the
         * rating is finalized.
         *
         * @param rating the pending rating. A value between 0 and 5.
         */
        void onPendingRating(int rating);

        /**
         * Notification that the user has selected a final rating.
         *
         * @param rating the final rating selected. A value between 0 and 5.
         */
        void onFinalRating(int rating);

        /**
         * Notification that the user has canceled the rating.
         */
        void onCancelRating();
    }
}
