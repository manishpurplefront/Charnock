package com.charnock.dev.pushnotification;

import android.content.Context;
import android.content.Intent;


public class CommonUtilities {

    // Google project id
    public static final String SENDER_ID = "93162003572";
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.purplefront.school.pushnotification.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";
    // give your server registration url here
    static final String SERVER_URL = "http://purplefront.net/school/pushnotification/register.php";
    /**
     * Tag used on log messages.
     */
    static final String TAG = "PurpleFront GCM";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
