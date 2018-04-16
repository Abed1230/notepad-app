package com.abed.notepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends AppCompatActivity {

    private static final String TAG = "TagActivity";

    private DatabaseReference dbRef;
    private DatabaseReference tagsRef;
    private DatabaseReference noteRef;

    private TagsAdapter adapter;

    private List<Tag> tags;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        final EditText et = findViewById(R.id.et_tag_name);
        final ListView lv  = findViewById(R.id.lv_tags);
        final Button btn = findViewById(R.id.btn_create);

        Intent intent = getIntent();
        String noteId = intent.getStringExtra("note_id");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().
                child("users").
                child(userId);
        tagsRef = dbRef.child("tags");
        tagsRef.addValueEventListener(tagsRefValueEventListener);
        noteRef = dbRef.child("notes").child(noteId);

        tags = new ArrayList<>();
        adapter = new TagsAdapter(this, tags);
        lv.setAdapter(adapter);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String etText = et.getText().toString();
                if (!etText.isEmpty() && !tagsContain(etText)) {
                    String text = "Create " + etText;
                    btn.setVisibility(View.VISIBLE);
                    btn.setText(text);
                } else {
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = view.findViewById(R.id.cb);
                cb.setChecked(!cb.isChecked());
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = dbRef.push().getKey();
                Tag tag = new Tag(id, et.getText().toString(), false);
                tags.add(tag);
                adapter.notifyDataSetChanged();
                tagsRef.child(id).setValue(tag);
                et.setText("");
                et.clearFocus();
            }
        });
    }

    private ValueEventListener tagsRefValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot tagSnapshot : dataSnapshot.getChildren()) {
                Tag tag = tagSnapshot.getValue(Tag.class);
                tags.add(tag);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private boolean tagsContain(String name) {
        for (Tag tag: tags) {
            if (tag.getName().equals(name))
                return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // add tags to note
        List<Tag> tags = adapter.getCheckedTags();
        noteRef.child("tags").setValue(tags);
    }
}
