package com.abed.notepad;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Abed on 04/20/2018.
 */

public class AlarmService extends IntentService {
    private static final String TAG = "AlarmService";
    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_CANCEL = "CANCEL";

    public AlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtras(intent.getExtras());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, intent.getIntExtra(Constants.KEY_NOTIF_ID, 0), i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (intent.getAction().equals(ACTION_CREATE)) {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, intent.getLongExtra(Constants.KEY_TRIGGER_TIME, 0), alarmIntent);
        } else if (intent.getAction().equals(ACTION_CANCEL)) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}
