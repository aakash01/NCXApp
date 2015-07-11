package com.anutanetworks.ncxapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.Alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/5/2015.
 */
public class AlarmGridAdapter extends ArrayAdapter {

    private Context context;
    private List<Alarm> alarms = new ArrayList<>();

    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds = new SparseBooleanArray();

    public AlarmGridAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, android.R.layout.simple_list_item_1, alarms);
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
    public Alarm getItem(int position) {
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
            gridItemView = inflater.inflate(R.layout.fragment_alarm_list_item, null);

        } else {
            gridItemView = (View) convertView;

        }
        Alarm alarmRecord = alarms.get(position);

        TextView alarmComponent = (TextView) gridItemView
                .findViewById(R.id.component);
        alarmComponent.setText(alarmRecord.getComponent());
        TextView alarmSpecType = (TextView) gridItemView
                .findViewById(R.id.alarmtype);
        alarmSpecType.setText(alarmRecord.getAlarmSpecType());
        TextView alarmState = (TextView) gridItemView
                .findViewById(R.id.alarmstate);
        alarmState.setText(alarmRecord.getAlarmState());

        if ("ACTIVE".equals(alarmRecord.getAlarmState())) {
            alarmState.setTextColor(Color.parseColor("#B71C1C"));
        } else if ("CLEARED".equals(alarmRecord.getAlarmState())) {
            alarmState.setTextColor(Color.parseColor("#00C853"));
        }

        TextView ack = (TextView) gridItemView.findViewById(R.id.acknw);
        if ("false".equals(String.valueOf(alarmRecord.isAcknowledged()))) {
            ack.setText("UnAck");
            ack.setTextColor(Color.parseColor("#B71C1C"));
        } else if ("true".equals(String.valueOf(alarmRecord.isAcknowledged()))) {
            ack.setText("Ack");
            ack.setTextColor(Color.parseColor("#00C853"));
        }


        ImageView severityIcon = (ImageView) gridItemView.findViewById(R.id.severityIcon);
        switch (alarmRecord.getSeverity()) {
            case "CRITICAL":
                severityIcon.setImageResource(R.drawable.ic_critical_48px);
                break;
            case "MAJOR":
                severityIcon.setImageResource(R.drawable.ic_major_48px);
                break;
            case "WARNING":
                severityIcon.setImageResource(R.drawable.ic_warning_48px);
                break;
            case "MINOR":
                severityIcon.setImageResource(R.drawable.ic_minor_48px);
                break;
            case "INFO":
                severityIcon.setImageResource(R.drawable.ic_info_48px);
        }
        gridItemView.setBackgroundColor(Color.TRANSPARENT);
        if (mSelectedItemsIds.get(position)) {
            severityIcon.setImageResource(R.drawable.ic_check_circle_black_48dp);
            gridItemView.setSelected(true);
            gridItemView.setBackgroundColor(Color.LTGRAY);
            Log.d("aakash", String.valueOf(gridItemView.isSelected()));
        }

        return gridItemView;
    }

    public void updateAlarmEntries(ArrayList<Alarm> alarms) {
        this.addAll(alarms);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public void updateItemsValue(boolean isAcknowledge) {
        for (int i = 0; i < mSelectedItemsIds.size(); i++) {
            Alarm selecteditem = getItem(mSelectedItemsIds.keyAt(i));
            selecteditem.setAcknowledged(isAcknowledge);
        }
        removeSelection();
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
