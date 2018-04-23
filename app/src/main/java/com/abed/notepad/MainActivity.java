package com.abed.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference notesRef;
    private NotesAdapter adapter;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesRef = ((MyApp)this.getApplication()).getDbRef().child(Constants.DB_KEY_NOTES);
        notesRef.addValueEventListener(valueEventListener);

        notes = new ArrayList<>();
        adapter = new NotesAdapter(this, notes);

        GridView gv = findViewById(R.id.gridView);
        gv.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewAndEditNoteActivity.class);
                intent.putExtra(Constants.KEY_NOTE_ID, notes.get(position).getId());
                startActivity(intent);
            }
        });

        gv.setMultiChoiceModeListener(multiChoiceModeListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            notes.clear();
            for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                Note note = noteSnapshot.getValue(Note.class);
                notes.add(note);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
        private int count;

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if (checked) {
                count++;
                adapter.setSelection(position, true);
            } else {
                count--;
                adapter.removeSelection(position);
            }
            mode.setTitle(count + getString(R.string.title_multi_choice));
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            count = 0;
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_test, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_delete:
                    count = 0;
                    HashMap<Integer, Boolean> selection = adapter.getSelection();
                    for (int pos : selection.keySet()) {
                        notesRef.child(notes.get(pos).getId()).removeValue();
                    }
                    adapter.clearSelection();
                    mode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
        }
    };
}