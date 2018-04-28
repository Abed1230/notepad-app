package com.abed.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abed on 04/15/2018.
 */

public class TagsAdapter extends BaseAdapter {

    private static final String TAG = "TagsAdapter";
    private Context context;
    private List<Tag> tags;
    private List<Tag> checkedTags;
    private List<Tag> toRemove;

    public TagsAdapter(Context context, List<Tag> tags, List<Tag> checkedTags) {
        this.context = context;
        this.tags = tags;
        this.checkedTags = checkedTags;
        toRemove = new ArrayList<>();
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
        // Todo user ViewHolder class

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.tv = ((TextView)convertView.findViewById(R.id.tv));
            holder.cb = (CheckBox)convertView.findViewById(R.id.cb);
            holder.cb.setOnCheckedChangeListener(checkedChangeListener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv.setText(tags.get(position).getName());
        holder.cb.setChecked(checkedTagsContain(tags.get(position).getId()));
        holder.cb.setClickable(false);
        holder.cb.setTag(position);

        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isPressed()) {
                buttonView.setPressed(false);
                int pos = (Integer) buttonView.getTag();
                if (checkedTagsContain(tags.get(pos).getId())) {
                    checkedTagsRemove(tags.get(pos).getId());
                } else {
                    checkedTags.add(tags.get(pos));
                }
            }
        }
    };

    public List<Tag> getCheckedTags() {
        return checkedTags;
    }

    private boolean checkedTagsContain(String id) {
        for (Tag tag : checkedTags) {
            if (tag.getId().equals(id))
                return true;
        }
        return false;
    }

    private void checkedTagsRemove(String id) {
        // ConcurrentModificationException if remove directly from checked tags
        // Could use Iterator instead
        //List<Tag> toRemove = new ArrayList<>();
        toRemove.clear();
        for (Tag tag : checkedTags) {
            if (tag.getId().equals(id)) {
                //checkedTags.remove(tag);
                toRemove.add(tag);
            }
        }
        checkedTags.removeAll(toRemove);
    }

    private static class ViewHolder {
        TextView tv;
        CheckBox cb;
    }
}
