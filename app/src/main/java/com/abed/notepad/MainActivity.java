package com.abed.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String ID_DEFAULT_TAG = "default";

    private FirebaseAuth auth;

    private DatabaseReference notesRef;
    private DatabaseReference tagsRef;

    private List<Note> notes;
    private List<Tag> tags;

    private NotesAdapter adapter;
    private SpinnerTagsAdapter tagsAdapter;

    private String spinTagsSelectedItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        notes = new ArrayList<>();
        adapter = new NotesAdapter(this, notes);

        tags = new ArrayList<>();
        tagsAdapter = new SpinnerTagsAdapter(this, tags);

        Spinner spinTags = findViewById(R.id.spin_tags);
        spinTags.setAdapter(tagsAdapter);

        spinTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Tag tag = (Tag)tagsAdapter.getItem(position);
                spinTagsSelectedItemId = tag.getId();
                adapter.getFilter().filter(tag.getId());

                invalidateOptionsMenu();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GridView gv = findViewById(R.id.gridView);
        gv.setEmptyView(findViewById(R.id.tv_empty_state));
        gv.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewAndEditNoteActivity.class);
                intent.putExtra(Constants.KEY_NOTE_ID, adapter.getItmId(position));
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

    private ValueEventListener notesRefValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            notes.clear();
            for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                Note note = noteSnapshot.getValue(Note.class);
                notes.add(0, note);
            }
            adapter.notifyDataSetChanged();
            adapter.getFilter().filter(spinTagsSelectedItemId);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener tagsRefValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            tags.clear();
            tags.add(new Tag(ID_DEFAULT_TAG, getString(R.string.main_activity_default_tag_title)));
            for (DataSnapshot tagSnapshot : dataSnapshot.getChildren()) {
                Tag tag = tagSnapshot.getValue(Tag.class);
                tags.add(tag);
            }
            tagsAdapter.notifyDataSetChanged();
            adapter.getFilter().filter(spinTagsSelectedItemId);
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
            mode.setTitle(count + " " + getString(R.string.main_activity_multi_choice_title));
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            count = 0;
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_cab, menu);
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
                        Note note = (Note) adapter.getItem(pos);
                        if (note.getReminder() != null)
                            deleteReminder(note.getReminder().getId());
                        notesRef.child(note.getId()).removeValue();
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

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.item_delete_tag);
        if (spinTagsSelectedItemId == null || spinTagsSelectedItemId.equals(ID_DEFAULT_TAG)) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete_tag:
                return true;
            case R.id.item_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAuthStatus();
    }

    private void checkAuthStatus() {
        // if is signed in
        if (auth.getCurrentUser() != null) {
            initDbRefs();
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
                    initDbRefs();
                } else {
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(MainActivity.this, getString(R.string.message_error_create_user),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initDbRefs() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().
                child(Constants.DB_KEY_USERS).
                child(auth.getCurrentUser().getUid());

        notesRef = dbRef.child(Constants.DB_KEY_NOTES);
        notesRef.addValueEventListener(notesRefValueEventListener);

        tagsRef = dbRef.child(Constants.DB_KEY_TAGS);
        tagsRef.addValueEventListener(tagsRefValueEventListener);
    }

    private void deleteReminder(int id) {
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra(Constants.KEY_NOTIF_ID, id);
        intent.setAction(AlarmService.ACTION_CANCEL);
        startService(intent);
    }

    @Override
    protected void onStop() {
        notesRef.removeEventListener(notesRefValueEventListener);
        tagsRef.removeEventListener(tagsRefValueEventListener);
        super.onStop();
    }
}