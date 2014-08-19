package com.projects.learnwords.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class FileManagerArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public FileManagerArrayAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.file_manager_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.file_manager_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        String s = values.get(position);

        if(s.startsWith("/")) {
            imageView.setImageResource(R.drawable.folder);
            textView.setText(s.substring(1));
        } else if(s.startsWith("..")) {
            imageView.setImageResource(R.drawable.home);
            textView.setText(s);
        } else if (s.endsWith(".txt")){
            imageView.setImageResource(R.drawable.txt_file);
            textView.setText(s);
        } else {
            imageView.setImageResource(R.drawable.file);
            textView.setText(s);
        }

        return rowView;
    }
}
