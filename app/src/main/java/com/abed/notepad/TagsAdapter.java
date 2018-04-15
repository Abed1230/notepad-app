package com.abed.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Abed on 04/15/2018.
 */

public class TagsAdapter extends BaseAdapter {
    
    private Context context;
    private List<String> tags;
    private HashMap<Integer, Boolean> selection;

    public TagsAdapter(Context context, List<String> tags) {
        this.context = context;
        this.tags = tags;
        selection = new HashMap<Integer, Boolean>();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.tv)).setText(tags.get(position));
        CheckBox cb = convertView.findViewById(R.id.cb);
        cb.setClickable(false);
        cb.setChecked(false);

        if (selection.get(position) != null)
            cb.setChecked(true);

        return convertView;
    }

    public void setSelection(int position, boolean value) {
        if (selection.get(position) != null) {
            selection.remove(position);
        } else {
            selection.put(position, value);
        }
        notifyDataSetChanged();
    }

    public void removeSelection(int position) {
        selection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    public HashMap<Integer, Boolean> getSelection() {
        return selection;
    }

}
