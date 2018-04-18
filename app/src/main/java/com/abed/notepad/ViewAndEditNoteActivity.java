package com.abed.notepad;

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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewAndEditNoteActivity extends AppCompatActivity {

    private static final String TAG = "ViewAndEditNoteActivity";
    private static final int SELECT_TAGS_REQUEST = 1;

    private EditText etTitle;
    private EditText etText;

    private DatabaseReference dbRef;
    private DatabaseReference noteRef;

    private String userId;
    private String noteId;
    private boolean save;
    private List<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_note);

        Intent intent = getIntent();
        noteId = intent.getStringExtra("note_id");
        save = true;
        tags = new ArrayList<>();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().
                child("users").
                child(userId);
        noteRef = dbRef.child("notes").child(noteId);
        noteRef.addListenerForSingleValueEvent(noteRefSingleValueEventListener);
    }

    private ValueEventListener noteRefSingleValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Note note = dataSnapshot.getValue(Note.class);
            etTitle.setVisibility(View.VISIBLE);
            etText.setVisibility(View.VISIBLE);
            etTitle.setText(note.getTitle());
            etText.setText(note.getText());

            if (note.getTags() != null) {
                tags = note.getTags();
                if (tags.size() > 0)
                    invalidateOptionsMenu();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void finish() {
        if (save)
            save();
        super.finish();
    }

    private void save() {
        String title = etTitle.getText().toString();
        String text = etText.getText().toString();
        // If note is not empty then save
        if (!title.isEmpty() || !text.isEmpty()) {
            String date = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date());
            noteRef.setValue(new Note(noteId, title, text, date, tags));
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
        inflater.inflate(R.menu.menu_view_and_edit_note, menu);
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
            case R.id.item_close:
                save = false;
                finish();
                return true;
            case R.id.item_delete:
                noteRef.removeValue();
                save = false;
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}