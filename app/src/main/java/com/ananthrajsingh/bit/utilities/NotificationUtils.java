package com.ananthrajsingh.bit.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.ananthrajsingh.bit.MainActivity;
import com.ananthrajsingh.bit.R;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by Ananth on 4/27/2018.
 * This class will have helper methods which will help create a notification for our application
 *
 */

public class NotificationUtils {

    public static final int BIT_PENDING_INTENT_ID = 4388;
    public static final int NOTIFICATION_ID = 5454;

    /**
     * This function will build the notification and display it. It will be helpful to see
     * https://developer.android.com/training/notify-user/build-notification.html
     * @param context required to build the notification
     */
    public static void remindUserToUpdate(Context context){

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * This function will create a PendingIntent which will be triggered when the notification
     * is clicked. We want MainActivity to open the click
     * @param context we need this to create intent
     * @return PendingIntent which opens MainActivity
     */
    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        /*
         * This takes a unique id for this intent, intent which must be triggered by this
         * pending intent and FLAG_UPDATE_CURRENT, so that if the intent is created again, keep the
         * intent but update the data
         */
        return PendingIntent.getActivity(context,
                BIT_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private static Bitmap largeIcon(Context context){

        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_logo);
        return largeIcon;
    }
}
