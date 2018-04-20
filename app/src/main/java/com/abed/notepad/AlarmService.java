package com.abed.notepad;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;

/**
 * Created by Abed on 04/20/2018.
 */

public class AlarmService extends IntentService {

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    private IntentFilter matcher;

    public AlarmService() {
        super("AlarmService");
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        String id = intent.getStringExtra("id");
        long time = intent.getLongExtra("time", 0);

        if (matcher.matchAction(action)) {
            execute(action, id, time);
        }
    }

    private void execute(String action, String id, long time) {
        AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id", id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (CREATE.equals(action)) {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, time, alarmIntent);
        } else if (CANCEL.equals(action)) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}
