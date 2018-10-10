package sa.com.stc.customviews.ChartViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fafdalotaibi on 13/03/2016.
 */
public abstract class BaseChart extends View {

    public float mCurrValue;
    public int STARTING_ANGLE;
    public int FINISHING_ANGLE;

    public BaseChart(Context context) {
        super(context);
    }

    public BaseChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height)
            size = height;

        else
            size = width;

        setMeasuredDimension(size, size);
    }

}
