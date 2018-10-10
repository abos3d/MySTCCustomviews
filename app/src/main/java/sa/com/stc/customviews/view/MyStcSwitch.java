package sa.com.stc.customviews.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Switch;

/**
 * Created by fahadHAlotaibi on 2/12/17.
 */

public class MyStcSwitch extends Switch {


    private static final String KEY_MAIN = "main";
    private static final String KEY_ENABLE = "enable";

    private OnCheckedChangeListener listener;

    private boolean enable;

    public MyStcSwitch(Context context) {
        super(context);
    }

    public MyStcSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        enable = isEnabled();
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
        this.listener = listener;
    }

    public void setValue(boolean isChecked) {
        super.setOnCheckedChangeListener(null);
        setChecked(isChecked);
        super.setOnCheckedChangeListener(listener);
    }

    public void toggleMyStcSwitch() {
        super.setOnCheckedChangeListener(null);
        super.toggle();
        super.setOnCheckedChangeListener(listener);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.setOnCheckedChangeListener(null);
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(KEY_MAIN));
            this.enable = ((Bundle) state).getBoolean(KEY_ENABLE, isEnabled());
            setEnabled(enable);
        } else
            super.onRestoreInstanceState(state);
        super.setOnCheckedChangeListener(listener);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MAIN, super.onSaveInstanceState());
        bundle.putBoolean(KEY_ENABLE, enable);
        return bundle;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.enable = enabled;
    }
}
