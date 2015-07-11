package com.anutanetworks.ncxapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.AlarmDetailItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/9/2015.
 */
public class AlarmDetailActivityAdapter extends ArrayAdapter {
    private Context context;
    private List<AlarmDetailItem> items = new ArrayList<>();

    private LayoutInflater inflater;

    public AlarmDetailActivityAdapter(Context context, ArrayList<AlarmDetailItem> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
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
            gridItemView = inflater.inflate(R.layout.activity_alarm_detail_list_item_, null);
        } else {
            gridItemView = (View) convertView;
        }
        AlarmDetailItem alarmdetail = items.get(position);

        TextView listTitle = (TextView) gridItemView
                .findViewById(R.id.name);
        listTitle.setText(alarmdetail.getName());
        TextView description = (TextView) gridItemView
                .findViewById(R.id.value);
        if ("false".equals(alarmdetail.getValue())) {
            description.setText("No");

        } else if ("true".equals(alarmdetail.getValue())) {
            description.setText("Yes");
        } else {
            description.setText(alarmdetail.getValue());
        }
        return gridItemView;
    }

    public void updateItems(List<AlarmDetailItem> items) {
        this.addAll(items);
        notifyDataSetChanged();
    }
}
