package com.getmebag.bag.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.IconTextView;
import android.widget.TextView;

import com.getmebag.bag.R;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by karthiktangirala on 4/19/15.
 */
public class SettingsListAdapter extends ArrayAdapter<SettingsListItem> {

    ArrayList<SettingsListItem> items;
    private LayoutInflater layoutInflater;

    @Inject
    public SettingsListAdapter(Context context, final LayoutInflater layoutInflater) {
        super(context, 0);
        this.layoutInflater = layoutInflater;
    }

    @Override
    public void addAll(Collection<? extends SettingsListItem> collection) {
        this.items = new ArrayList<>(collection);
        super.addAll(this.items);
    }

    @Override
    public int getCount() {
        return (items != null ?  items.size() : 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater
                    .inflate(R.layout.list_item_settings, null);
            holder = new ViewHolder();

            holder.mainIcon = (IconTextView) convertView.findViewById(R.id.settings_list_main_icon);
            holder.title = (TextView) convertView.findViewById(R.id.settings_list_title);
            holder.separator = convertView.findViewById(R.id.settings_list_bottom_separator);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setUp(holder, position);

        return convertView;
    }

    private void setUp(ViewHolder holder, int position) {
        SettingsListItem item = items.get(position);
        holder.mainIcon.setText(item.getMainIcon());
        holder.title.setText(item.getTitle());
    }

    static class ViewHolder {
        IconTextView mainIcon;
        TextView title;
        View separator;
    }

}
