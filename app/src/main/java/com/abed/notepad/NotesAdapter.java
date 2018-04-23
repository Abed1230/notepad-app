package com.abed.notepad;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Abed on 03/31/2018.
 */

public class NotesAdapter extends BaseAdapter {
    private Context context;
    private List<Note> notes;
    private HashMap<Integer, Boolean> selection;

    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
        selection = new HashMap<>();
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        String title = notes.get(position).getTitle();
        TextView tvTitle = convertView.findViewById(R.id.tv_title);
        // Set gone by default
        tvTitle.setVisibility(View.GONE);
        // If title contains text set tv title to visible
        if (!title.isEmpty()) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        ((TextView)convertView.findViewById(R.id.tv_text)).setText(notes.get(position).getText());
        ((TextView)convertView.findViewById(R.id.tv_date)).setText(notes.get(position).getDate());

        convertView.setBackgroundColor(Color.WHITE);
        if (selection.get(position) != null) {
            convertView.setBackgroundColor(Color.CYAN);
        }

        return convertView;
    }

    public void setSelection(int position, boolean value) {
        selection.put(position, value);
        notifyDataSetChanged();
    }

    public void removeSelection(int position) {
        selection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selection = new HashMap<>();
        notifyDataSetChanged();
    }

    public HashMap<Integer, Boolean> getSelection() {
        return selection;
    }

}
