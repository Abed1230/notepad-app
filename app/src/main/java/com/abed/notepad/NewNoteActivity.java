package com.abed.notepad;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class NewNoteActivity extends AppCompatActivity {

    private static final String TAG = "NewNoteActivity";
    private static final int SELECT_TAGS_REQUEST = 1;
    private static final int PICK_DATE_AND_TIME_REQUEST = 2;

    private EditText etTitle;
    private EditText etText;

    private DatabaseReference dbRef;
    private DatabaseReference notesRef;

    private String userId;
    private List<String> tags;

    private Reminder reminder;

    //private boolean wantsReminder;
    //private long reminderTriggerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_note);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().
                child("users").
                child(userId);
        notesRef = dbRef.child("notes");
        //tagsRef = dbRef.child("tags");
        tags = new ArrayList<>();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemTag = menu.findItem(R.id.item_tag);
        MenuItem itemReminder = menu.findItem(R.id.item_reminder);

        if (tags.size() > 0) {
            itemTag.setTitle("Edit tag");
        } else {
            itemTag.setTitle("Add tag");
        }

        if (reminder != null) {
            itemReminder.setTitle("Edit reminder");
        } else {
            itemReminder.setTitle("Add reminder");
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_tag:
                Intent intent = new Intent(this, TagActivity.class);
                intent.putStringArrayListExtra("tags", (ArrayList)tags);
                startActivityForResult(intent, SELECT_TAGS_REQUEST);
                return true;
            case R.id.item_reminder:
                Intent intent2 = new Intent(this, DateAndTimePickerActivity.class);
                if (reminder != null) {
                    intent2.putExtra("trigger_time", reminder.getTriggerTime());
                }
                startActivityForResult(intent2, PICK_DATE_AND_TIME_REQUEST );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_TAGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get selected tags
                Log.d(TAG, "selected tags: " +
                        data.getStringArrayListExtra("tags").toString());

                tags = data.getStringArrayListExtra("tags");
                invalidateOptionsMenu();
            }
        } else if (requestCode == PICK_DATE_AND_TIME_REQUEST) {
            if (resultCode == RESULT_OK) {
                //wantsReminder = true;
                //reminderId = new Random().nextInt(50);
                //reminderTriggerTime = data.getLongExtra("time_in_millis", 0);
                String action = data.getAction();
                if (action.equals(DateAndTimePickerActivity.ACTION_ADD)) {
                    int id = (int)(System.currentTimeMillis()/1000);
                    long triggerTime = data.getLongExtra("time_in_millis", 0);
                    reminder = new Reminder(id, triggerTime);
                } else if (action.equals(DateAndTimePickerActivity.ACTION_DELETE)) {
                    reminder = null;
                }
                invalidateOptionsMenu();
            }
        }
    }

    @Override
    public void finish() {
        save();
        super.finish();
    }

    private void save() {
        String title = etTitle.getText().toString();
        String text = etText.getText().toString();
        // If note is not empty then save
        if (!title.isEmpty() || !text.isEmpty()) {
            String date = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date());
            String id = notesRef.push().getKey();
            if (reminder != null) {
                setReminder(title, text, id, reminder.getId());
                notesRef.child(id).setValue(new Note(id, title, text, date, tags, reminder));
            } else {
                notesRef.child(id).setValue(new Note(id, title, text, date, tags, null));
            }
        }
    }

    private void setReminder(String title, String text, String tag, int id) {
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("trigger_time", reminder.getTriggerTime());
        intent.putExtra("notif_title", title);
        intent.putExtra("notif_text", text);
        intent.putExtra("notif_tag", tag);
        intent.putExtra("notif_id", id);
        intent.setAction(AlarmService.ACTION_CREATE);
        startService(intent);
    }
}