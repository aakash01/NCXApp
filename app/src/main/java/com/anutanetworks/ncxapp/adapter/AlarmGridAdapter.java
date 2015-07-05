package com.anutanetworks.ncxapp.adapter;

import android.content.Context;
import android.graphics.Color;
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
            gridItemView = inflater.inflate(R.layout.fragment_alarm_list_item, null);

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
            TextView severityType = (TextView) gridItemView
                    .findViewById(R.id.severityType);
            severityType.setText(alarmRecord.getSeverity());
            if("ACTIVE".equals(alarmRecord.getAlarmState())){
                alarmState.setTextColor(Color.parseColor("#B71C1C"));
            }else if("CLEARED".equals(alarmRecord.getAlarmState())){
                alarmState.setTextColor(Color.parseColor("#00C853"));
            }
            if(alarmRecord.isAcknowledged()) {
                gridItemView.setBackgroundResource(R.color.transp);
            }

            ImageView severityIcon = (ImageView) gridItemView.findViewById(R.id.severityIcon);
            switch (alarmRecord.getSeverity()){
                case "CRITICAL":
                    severityIcon.setImageResource(R.drawable.ic_severity_critical);
                    break;
                case "MAJOR":
                    severityIcon.setImageResource(R.drawable.ic_severity_major);
                    break;
                case "WARNING":
                case "MINOR":
                    severityIcon.setImageResource(R.drawable.ic_severity_minor);
                    break;
                case "INFO":
                    severityIcon.setImageResource(R.drawable.ic_severity_info);
            }

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
