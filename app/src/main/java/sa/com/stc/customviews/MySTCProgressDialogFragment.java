package sa.com.stc.customviews;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by fahadHAlotaibi on 9/25/16.
 */

public class MySTCProgressDialogFragment extends DialogFragment {

    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_INDETERMINATE = "indeterminate";
    private static final String EXTRA_TAG = "tag";
    private String TAG = "MySTCProgressDialogFragment";
    private String message;
    private String title;
    private boolean indeterminate = true;
    private ProgressDialog dialog;

    public static MySTCProgressDialogFragment newInstance(String TAG, String title, String message, boolean indeterminate) {

        Bundle args = new Bundle();

        if (TAG != null)
            args.putString(EXTRA_TAG, TAG);

        if (title != null)
            args.putString(EXTRA_TITLE, title);

        if (message != null)
            args.putString(EXTRA_MESSAGE, message);

        args.putBoolean(EXTRA_INDETERMINATE, indeterminate);

        MySTCProgressDialogFragment fragment = new MySTCProgressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MySTCProgressDialogFragment newInstance(String title, String message, boolean indeterminate) {
        return newInstance(null, title, message, indeterminate);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        if (savedInstanceState == null)
            savedInstanceState = getArguments();

        if (savedInstanceState.containsKey(EXTRA_TAG))
            TAG = savedInstanceState.getString(EXTRA_TAG);

        if (savedInstanceState.containsKey(EXTRA_TITLE))
            title = savedInstanceState.getString(EXTRA_TITLE);

        if (savedInstanceState.containsKey(EXTRA_MESSAGE))
            message = savedInstanceState.getString(EXTRA_MESSAGE);

        if (savedInstanceState.containsKey(EXTRA_INDETERMINATE))
            indeterminate = savedInstanceState.getBoolean(EXTRA_INDETERMINATE);

        dialog.setIndeterminate(indeterminate);
        dialog.setMessage(message);
        dialog.setTitle(title);
        return dialog;
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (title != null)
            outState.putString(EXTRA_TITLE, title);

        if (message != null)
            outState.putString(EXTRA_MESSAGE, message);

        outState.putBoolean(EXTRA_INDETERMINATE, indeterminate);

        super.onSaveInstanceState(outState);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        if (dialog != null)
            dialog.setMessage(message);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }
}
