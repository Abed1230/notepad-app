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

public class NewNoteActivity extends AppCompatActivity {

    private static final String TAG = "NewNoteActivity";
    private static final int SELECT_TAGS_REQUEST = 1;

    private EditText etTitle;
    private EditText etText;

    private DatabaseReference dbRef;
    private DatabaseReference notesRef;

    private String userId;
    private List<String> tags;

    //private String id;

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
    public void finish() {
        Log.d(TAG, "finish method");
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
            notesRef.child(id).setValue(new Note(id, title, text, date, tags));
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
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.item_tag);
        if (tags.size() > 0) {
            item.setTitle("Edit tag");
        } else {
            item.setTitle("Add tag");
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}