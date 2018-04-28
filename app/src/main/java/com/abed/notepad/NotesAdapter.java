package com.abed.notepad;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Abed on 03/31/2018.
 */

public class NotesAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Note> notes;
    private HashMap<Integer, Boolean> selection;

    private List<Note> originalValues;
    private MyFilter filter;

    public NotesAdapter(Context context, List<Note> notes) {
        Log.d("NotesAdapter", "constructor");
        this.context = context;
        this.notes = notes;
        selection = new HashMap<>();
        originalValues = notes;
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView)convertView.findViewById(R.id.tv_title);
            holder.tvText = (TextView)convertView.findViewById(R.id.tv_text);
            holder.tvDate = (TextView)convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        String title = notes.get(position).getTitle();
        TextView tvTitle = holder.tvTitle;
        // Set gone by default
        tvTitle.setVisibility(View.GONE);
        // If title contains text set tv title to visible
        if (!title.isEmpty()) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        holder.tvText.setText(notes.get(position).getText());
        holder.tvDate.setText(notes.get(position).getDate());

        convertView.setBackgroundResource(R.color.white);
        if (selection.get(position) != null) {
            convertView.setBackgroundResource(R.color.grey);
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new MyFilter();
        }
        return filter;
    }

    public String getItmId(int position) {
        return notes.get(position).getId();
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

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvText;
        TextView tvDate;
    }

    private class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                List<Note> list = new ArrayList<>(originalValues);
                results.count = list.size();
                results.values = list;
            } else {
                String tagId = constraint.toString();
                List<Note> filteredNotes = new ArrayList<>();

                if (tagId.equals(MainActivity.ID_DEFAULT_TAG)) {
                    filteredNotes.addAll(originalValues);
                } else {
                    for (Note note : originalValues) {
                        if (note.getTags() != null) {
                            for (Tag tag : note.getTags()) {
                                if (tag.getId().equals(tagId)) {
                                    filteredNotes.add(note);
                                }
                            }
                        }
                    }
                }

                results.count = filteredNotes.size();
                results.values = filteredNotes;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes = (ArrayList<Note>)results.values;
            notifyDataSetChanged();
        }
    }
}
