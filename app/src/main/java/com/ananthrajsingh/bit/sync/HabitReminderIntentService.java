package com.ananthrajsingh.bit.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import static com.ananthrajsingh.bit.utilities.NotificationUtils.clearAllNotifications;

/**
 * Created by Ananth on 4/29/2018.
 *
 */

public class HabitReminderIntentService extends IntentService {

    public static final String ACTION_DISMISS_NOTIFICATION = "action-dismiss";

    public HabitReminderIntentService(){
        super("HabitReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if(ACTION_DISMISS_NOTIFICATION.equals(action)){
            Log.e("HabitReminderIntentServ", "We are in if(ACTION_DISMISS_NOTIFICATION.equals(action))");
            clearAllNotifications(this);
        }
    }
}
