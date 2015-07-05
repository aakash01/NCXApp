package com.anutanetworks.ncxapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.Alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/5/2015.
 */
public class AlarmGridAdapter extends ArrayAdapter{

    private Context context;
    private List<Alarm> alarms = new ArrayList<>();

    private LayoutInflater inflater;

    public AlarmGridAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context,android.R.layout.simple_list_item_1,alarms);
        this.context = context;
        this.alarms = alarms;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridItemView;

        if (convertView == null) {
            gridItemView = new View(context);
            gridItemView = inflater.inflate(R.layout.fragment_alarm_grid_item, null);
            TextView textView = (TextView) gridItemView
                    .findViewById(R.id.grid_item_label1);
            textView.setText(alarms.get(position).getAlarmSpecType());
            TextView textView2 = (TextView) gridItemView
                    .findViewById(R.id.grid_item_label2);
            textView2.setText(alarms.get(position).getMessage());

        } else {
            gridItemView = (View) convertView;
        }

        return gridItemView;
    }

    public void updateAlarmEntries(ArrayList<Alarm> alarms) {
        this.addAll(alarms);
        notifyDataSetChanged();
    }
}
