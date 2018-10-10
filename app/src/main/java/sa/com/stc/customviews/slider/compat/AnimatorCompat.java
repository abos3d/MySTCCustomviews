package sa.com.stc.customviews.slider.compat;

import android.os.Build;

/**
 * Created by fahad on 04/05/2015.
 */
public abstract class AnimatorCompat {
    AnimatorCompat() {

    }

    public static final AnimatorCompat create(float start, float end, AnimationFrameUpdateListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return new AnimatorCompatV11(start, end, listener);
        } else {
            return new AnimatorCompatBase(start, end, listener);
        }
    }

    public abstract void cancel();

    public abstract boolean isRunning();

    public abstract void setDuration(int progressAnimationDuration);

    public abstract void start();

    public interface AnimationFrameUpdateListener {
        void onAnimationFrame(float currentValue);
    }

    private static class AnimatorCompatBase extends AnimatorCompat {

        private final AnimationFrameUpdateListener mListener;
        private final float mEndValue;

        public AnimatorCompatBase(float start, float end, AnimationFrameUpdateListener listener) {
            mListener = listener;
            mEndValue = end;
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isRunning() {
            return false;
        }

        @Override
        public void setDuration(int progressAnimationDuration) {

        }

        @Override
        public void start() {
            mListener.onAnimationFrame(mEndValue);
        }
    }
}
