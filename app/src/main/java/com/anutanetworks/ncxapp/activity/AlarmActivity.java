package com.anutanetworks.ncxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.adapter.AlarmDetailActivityAdapter;
import com.anutanetworks.ncxapp.model.Alarm;
import com.anutanetworks.ncxapp.model.AlarmDetailItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Aakash on 7/8/2015.
 */
public class AlarmActivity extends AppCompatActivity {


    // private List<AlarmDetail> alarmDetail;
    private AlarmDetailActivityAdapter alarmAdapter;
    private AbsListView mListView;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_detail);
        alarmAdapter = new AlarmDetailActivityAdapter(getApplicationContext(), new ArrayList<AlarmDetailItem>());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Alarm Detail");
        actionBar.setDisplayUseLogoEnabled(false);

        mListView = (AbsListView) findViewById(R.id.alarmlist);
        ((AdapterView<ListAdapter>) mListView).setAdapter(alarmAdapter);
        Intent i = getIntent();
        Alarm alarmObj = (Alarm) i.getSerializableExtra("alarmObject");
        final List<AlarmDetailItem> items = new ArrayList<>();
        items.add(new AlarmDetailItem("Severity", alarmObj.getSeverity()));
        items.add(new AlarmDetailItem("Component", alarmObj.getComponent()));
        items.add(new AlarmDetailItem("Type", alarmObj.getAlarmSpecType()));
        items.add(new AlarmDetailItem("Description", alarmObj.getDescription()));
        items.add(new AlarmDetailItem("Alarm State", alarmObj.getAlarmState()));
        items.add(new AlarmDetailItem("Message", alarmObj.getMessage()));
        String longV = String.valueOf(alarmObj.getTimeStamp());
        long millisecond = Long.parseLong(longV);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
        items.add(new AlarmDetailItem("Time Of Last Update", String.valueOf(dateString)));
        items.add(new AlarmDetailItem("Acknowledged ", String.valueOf(alarmObj.isAcknowledged())));
        String longupdate = String.valueOf(alarmObj.getLastActiveTimeStamp());
        long lonupdatetime = Long.parseLong(longupdate);
        String dateString1 = DateFormat.format("MM/dd/yyyy", new Date(lonupdatetime)).toString();
        items.add(new AlarmDetailItem("Time Of Last Update", String.valueOf(dateString1)));

        alarmAdapter.updateItems(items);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
