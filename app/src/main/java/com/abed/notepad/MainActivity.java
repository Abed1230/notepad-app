package com.abed.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gv = findViewById(R.id.gridView);
        NotesAdapter adapter = new NotesAdapter(this, null);
        gv.setAdapter(adapter);
    }
}