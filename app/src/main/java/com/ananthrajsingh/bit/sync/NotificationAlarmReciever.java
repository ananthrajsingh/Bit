package com.ananthrajsingh.bit.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.ananthrajsingh.bit.utilities.NotificationUtils.remindUserToUpdate;

/**
 * Created by Ananth on 4/29/2018.
 *
 */

public class NotificationAlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        remindUserToUpdate(context);
    }
}
