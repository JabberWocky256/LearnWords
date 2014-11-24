package com.projects.learnwords.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.projects.learnwords.app.R;

import java.util.Map;

public class StartGameArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final Map<String, Boolean> list;
    private final String[] keyList;

    public StartGameArrayAdapter(Context context, Map<String, ?> list) {
        super(context, R.layout.dictionary_start_game_row, list.keySet().toArray(new String[0]));
        this.context = context;
        this.list = (Map<String, Boolean>)list;
        this.keyList = list.keySet().toArray(new String[0]);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.dictionary_start_game_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        String key = keyList[position];

        textView.setText(key);

        if(list.get(key) == true) {
            imageView.setImageResource(R.drawable.learned);
        }

        return rowView;
    }
}