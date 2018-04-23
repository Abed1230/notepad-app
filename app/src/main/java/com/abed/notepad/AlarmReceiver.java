package com.abed.notepad;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abed on 04/20/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(Constants.KEY_NOTIF_TITLE);
        String text = intent.getStringExtra(Constants.KEY_NOTIF_TEXT);
        String tag = intent.getStringExtra(Constants.KEY_NOTIF_TAG);
        int id = intent.getIntExtra(Constants.KEY_NOTIF_ID, 0);

        // Todo: view note when notification pressed
        /*
        Intent i = new Intent(context, ViewAndEditNoteActivity.class);
        i.putExtra("note_id", tag);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        */
        // Show notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.ID_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(tag, id, builder.build());

        // Remove reminder from db
        ((MyApp)context.getApplicationContext()).getDbRef().
                child(Constants.DB_KEY_NOTES).
                child(tag).
                child(Constants.DB_KEY_VALUE_REMINDER).removeValue();
    }
}
