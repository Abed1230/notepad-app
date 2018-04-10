package com.abed.notepad;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etText;

    private DatabaseReference database;
    private FirebaseAuth auth;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_note);

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        key = database.child("notes").child(auth.getCurrentUser().getUid()).push().getKey();

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
            Note note = new Note(title, text, date, "none");

            database.child("notes").child(auth.getCurrentUser().getUid()).child(key).setValue(note);
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
