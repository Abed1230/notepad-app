package com.abed.notepad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Abed on 04/20/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Hello there!! id: " + intent.getStringExtra("id"), Toast.LENGTH_LONG).show();
    }
}
