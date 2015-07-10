package com.anutanetworks.ncxapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.Alarm;

/**
 * Created by Aakash on 7/8/2015.
 */
public class AlarmActivity extends Activity {
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        setContentView(R.layout.alarm_detail);

        updateAlarmDetail();


    }

    private void updateAlarmDetail() {
        Alarm item = new Alarm();

        TextView desc = (TextView) findViewById(R.id.alarm_des);
        TextView msg = (TextView) findViewById(R.id.alarm_msg);
        TextView lastActive = (TextView) findViewById(R.id.alarm_lastAct);
        TextView ack = (TextView) findViewById(R.id.alarm_ack);
        TextView update = (TextView) findViewById(R.id.alarm_tolu);

        if (id == item.getId()) {
            desc.setText(item.getDescription());


        }


    }
}
