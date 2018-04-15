package com.abed.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        final EditText et = findViewById(R.id.et_tag_name);
        ListView lv  = findViewById(R.id.lv_tags);
        final Button btn = findViewById(R.id.btn_create);

        final List<String> tags = new ArrayList<>();
        tags.add("Apple");
        tags.add("Banana");
        final TagsAdapter adapter = new TagsAdapter(this, tags);
        lv.setAdapter(adapter);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String etText = et.getText().toString();
                if (!etText.isEmpty() && !tags.contains(etText)) {
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
                adapter.setSelection(position, true);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags.add(et.getText().toString());
                adapter.setSelection(tags.indexOf(et.getText().toString()), true);
                //adapter.notifyDataSetChanged();
                et.setText("");
            }
        });
    }
}
