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
        String title = intent.getStringExtra("notif_title");
        String text = intent.getStringExtra("notif_text");
        String tag = intent.getStringExtra("notif_tag");
        int id = intent.getIntExtra("notif_id", 0);
        /*
        Intent i = new Intent(context, ViewAndEditNoteActivity.class);
        i.putExtra("note_id", tag);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"reminders")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(tag, id, builder.build());

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().
                child("users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference noteRef = dbRef.child("notes").child(tag);
        noteRef.child("reminder").removeValue();
    }
}
