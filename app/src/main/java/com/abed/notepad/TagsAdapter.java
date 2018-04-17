package com.abed.notepad;

import android.content.Context;
import android.util.Log;
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
    
    private Context context;
    private List<String> tags;
    private List<String> checkedTags;

    public TagsAdapter(Context context, List<String> tags, List<String> checkedTags) {
        this.context = context;
        this.tags = tags;
        this.checkedTags = checkedTags;
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
        cb.setChecked(checkedTags.contains(tags.get(position)));
        cb.setClickable(false);
        cb.setTag(position);
        cb.setOnCheckedChangeListener(checkedChangeListener);

        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int pos = (Integer) buttonView.getTag();

            if (checkedTags.contains(tags.get(pos))) {
                checkedTags.remove(checkedTags.indexOf(tags.get(pos)));
            } else {
                checkedTags.add(tags.get(pos));
            }
        }
    };


    public List<String> getCheckedTags() {
        return checkedTags;
    }
}
