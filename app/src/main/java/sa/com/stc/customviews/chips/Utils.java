package sa.com.stc.customviews.chips;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;

/**
 * Created by fahadHAlotaibi on 10/2/16.
 */

public class Utils {

    public static final Uri getUriToDrawable(@NonNull Context context, @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    public static String phoneNumberValidator(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) return phoneNumber;

        if (phoneNumber.startsWith("+")) {
            phoneNumber = phoneNumber.replace("+", "00");
        }

        if (phoneNumber.startsWith("966")) {
            phoneNumber = phoneNumber.replace("966", "0");
        }

        if (phoneNumber.startsWith("00966")) {
            phoneNumber = phoneNumber.replace("00966", "0");
        }

        return phoneNumber;
    }

}
