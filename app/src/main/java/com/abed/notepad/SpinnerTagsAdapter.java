package com.abed.notepad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abed on 04/15/2018.
 */

public class SpinnerTagsAdapter extends ArrayAdapter<Tag> {

    private static final String TAG = "SpinnerTagsAdapter";
    private Context context;
    private List<Tag> tags;

    public SpinnerTagsAdapter(Context context, List<Tag> tags) {
        super(context, R.layout.spinner_item, tags);
        this.context = context;
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Tag getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.tv)).setText(tags.get(position).getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.spinner_drop_down_view, null);
        }

        ((TextView)convertView.findViewById(R.id.tv)).setText(tags.get(position).getName());

        return convertView;
    }
}
