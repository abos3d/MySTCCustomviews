package sa.com.stc.customviews;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    public static final String ENGLISH = "en";
    public static final String ARABIC = "ar";
    private static String language;

    public static void setLanguageForContext(Context context) {
        language = context.getResources().getConfiguration().locale.getLanguage();
    }

    public static String localizeNumberString(String number) {
        if (number == null)
            return "";

        String ret = number;

        if (!isArabicSelected())
            return ret;

        try {
            if (ret.startsWith("-"))
                ret = ret.replace("-", "").concat("-");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        for (int i = 0; i <= 9; i++)
            ret = ret.replace("" + i, "" + (char) (i + 1632));

        return ret;
    }

    public static boolean isArabicSelected() {
        return language.equals(ARABIC);
    }

    public static String getLanguage(Context context) {
        if (language == null)
            setLanguageForContext(context);
        return language;
    }


    public static void hideSoftKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

}
