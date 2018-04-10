package com.abed.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewAndEditNoteActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etText;

    DatabaseReference database;

    private String id;

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
        id = intent.getStringExtra("note_id");

        database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = database.child("notes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                etTitle.setText(note.getTitle());
                etText.setText(note.getText());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    private void save() {
        String title = etTitle.getText().toString();
        String text = etText.getText().toString();
        // If note is not empty then save
        if (!title.isEmpty() || !text.isEmpty()) {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Note note = new Note(id, title, text, date, "none");

            database.child("notes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).setValue(note);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
