package com.abed.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abed on 04/15/2018.
 */

public class SpinnerTagsAdapter extends BaseAdapter {

    private static final String TAG = "SpinnerTagsAdapter";
    private Context context;
    private List<Tag> tags;

    public SpinnerTagsAdapter(Context context, List<Tag> tags) {
        this.context = context;
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        ((CheckedTextView)convertView.findViewById(android.R.id.text1)).setText(tags.get(position).getName());

        return convertView;
    }
}
