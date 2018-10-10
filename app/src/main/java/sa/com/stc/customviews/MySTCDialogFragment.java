package sa.com.stc.customviews;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

/**
 * Created by fahadalotaibi on 9/21/16.
 */

public class MySTCDialogFragment extends DialogFragment {

    private static final String EXTRA_IS_SHOWING = "showing";
    private static final String EXTRA_cancelable = "cancelable";
    private static final String EXTRA_styleId = "styleId";
    private static final String EXTRA_positiveButtonText = "positiveButtonText";
    private static final String EXTRA_negativeButtonText = "negativeButtonText";
    private static final String EXTRA_title = "title";
    private static final String EXTRA_message = "message";
    @StyleRes
    private int styleId;
    private boolean cancelable = false;
    private DialogInterface.OnClickListener positiveButtonListener;
    private DialogInterface.OnClickListener negativeButtonListener;
    private String positiveButtonText;
    private String negativeButtonText;
    private String title;
    private String message;
    private boolean isShowing = false;

    public static MySTCDialogFragment newInstance() {

        Bundle args = new Bundle();

        MySTCDialogFragment fragment = new MySTCDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_cancelable))
                cancelable = savedInstanceState.getBoolean(EXTRA_cancelable);

            if (savedInstanceState.containsKey(EXTRA_positiveButtonText))
                positiveButtonText = savedInstanceState.getString(EXTRA_positiveButtonText);

            if (savedInstanceState.containsKey(EXTRA_negativeButtonText))
                negativeButtonText = savedInstanceState.getString(EXTRA_negativeButtonText);

            if (savedInstanceState.containsKey(EXTRA_title))
                title = savedInstanceState.getString(EXTRA_title);

            if (savedInstanceState.containsKey(EXTRA_message))
                message = savedInstanceState.getString(EXTRA_message);

            if (savedInstanceState.containsKey(EXTRA_IS_SHOWING))
                isShowing = savedInstanceState.getBoolean(EXTRA_IS_SHOWING);

            if (savedInstanceState.containsKey(EXTRA_styleId))
                styleId = savedInstanceState.getInt(EXTRA_styleId);
        }

        return new AlertDialog.Builder(getContext(), styleId)
                .setCancelable(cancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (positiveButtonListener != null)
                            positiveButtonListener.onClick(dialog, which);
                        else dialog.dismiss();
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (negativeButtonListener != null)
                            negativeButtonListener.onClick(dialog, which);
                        else dialog.dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_IS_SHOWING, isShowing);
        outState.putBoolean(EXTRA_cancelable, cancelable);

        if (positiveButtonText != null)
            outState.putString(EXTRA_positiveButtonText, positiveButtonText);

        if (negativeButtonText != null)
            outState.putString(EXTRA_negativeButtonText, negativeButtonText);

        if (title != null)
            outState.putString(EXTRA_title, title);

        if (message != null)
            outState.putString(EXTRA_message, message);

        outState.putInt(EXTRA_styleId, styleId);

    }


    public MySTCDialogFragment setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public MySTCDialogFragment setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public MySTCDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public MySTCDialogFragment setMessage(String message) {
        this.message = message;
        return this;
    }

    public MySTCDialogFragment setPositiveButtonListener(DialogInterface.OnClickListener positiveButtonListener) {
        this.positiveButtonListener = positiveButtonListener;
        return this;
    }

    public MySTCDialogFragment setNegativeButtonListener(DialogInterface.OnClickListener negativeButtonListener) {
        this.negativeButtonListener = negativeButtonListener;
        return this;
    }

    public MySTCDialogFragment setCancelableDialog(boolean cancelable) {
        this.cancelable = cancelable;
        setCancelable(cancelable);
        return this;
    }

    public MySTCDialogFragment setStyleId(@StyleRes int styleId) {
        this.styleId = styleId;
        return this;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        isShowing = true;
        return super.show(transaction, tag);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        isShowing = true;
    }

    public boolean isShowing() {
        return isShowing;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        isShowing = false;
    }
}
