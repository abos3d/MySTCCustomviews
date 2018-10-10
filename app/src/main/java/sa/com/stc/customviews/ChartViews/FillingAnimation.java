package sa.com.stc.customviews.ChartViews;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FillingAnimation extends Animation {

    private float sweepAngle;
    private BaseChart chart;

    public FillingAnimation(BaseChart chart, float sweepAngle, long duration) {
        this.chart = chart;
        this.sweepAngle = sweepAngle;
        setDuration(duration);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        chart.mCurrValue = (int) (chart.FINISHING_ANGLE + ((sweepAngle - chart.FINISHING_ANGLE) * interpolatedTime));
        chart.invalidate();
    }
}