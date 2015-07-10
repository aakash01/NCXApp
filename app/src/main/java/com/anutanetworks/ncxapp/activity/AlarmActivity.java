package com.anutanetworks.ncxapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.adapter.AlarmDetailActivityAdapter;
import com.anutanetworks.ncxapp.model.Alarm;
import com.anutanetworks.ncxapp.model.AlarmDetail;
import com.anutanetworks.ncxapp.model.AlarmDetailItem;
import com.anutanetworks.ncxapp.model.Approval;
import com.anutanetworks.ncxapp.model.Capacity;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Aakash on 7/8/2015.
 */
public class AlarmActivity extends Activity {


    // private List<AlarmDetail> alarmDetail;
    private AlarmDetailActivityAdapter alarmAdapter;
    private AbsListView mListView;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_detail);

        alarmAdapter = new AlarmDetailActivityAdapter(getApplicationContext(), new ArrayList<AlarmDetailItem>());

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

}
