package com.abed.notepad;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abed on 04/18/2018.
 */

public class MyApp extends Application {

    private static final String TAG = "MyApp";
    private FirebaseAuth auth;
    private DatabaseReference dbRef;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        createNotificationChannel();
        auth = FirebaseAuth.getInstance();
        checkAuthStatus();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_title);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Constants.ID_NOTIFICATION_CHANNEL, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkAuthStatus() {
        // if is signed in
        if (auth.getCurrentUser() != null) {
            setDbRef();
        } else {
            signInAnonymously();
        }
    }

    private void signInAnonymously() {
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInAnonymously:success");
                    setDbRef();
                } else {
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(MyApp.this, getString(R.string.message_error_create_user),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDbRef() {
        dbRef = FirebaseDatabase.getInstance().getReference().
                child(Constants.DB_KEY_USERS).
                child(auth.getCurrentUser().getUid());
    }

    public DatabaseReference getDbRef() {
        return dbRef;
    }
}
