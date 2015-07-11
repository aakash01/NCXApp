package com.anutanetworks.ncxapp.activity.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.adapter.AlarmDetailActivityAdapter;
import com.anutanetworks.ncxapp.model.Alarm;
import com.anutanetworks.ncxapp.model.AlarmDetailItem;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Aakash on 7/8/2015.
 */
public class AlarmActivity extends AppCompatActivity {


    private AlarmDetailActivityAdapter alarmAdapter;
    private AbsListView mListView;
    String id;
    Alarm alarmObj = new Alarm();

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
         alarmObj = (Alarm) i.getSerializableExtra("alarmObject");
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

        getMenuInflater().inflate(R.menu.menu_alarm_list, menu);
        if (alarmObj.isAcknowledged()) {
            menu.getItem(0).setVisible(false);
        }
        else {
            menu.getItem(1).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String posturl = null;
        boolean isAck = false;

        switch(item.getItemId())
        {
            case R.id.Ack:
                posturl = "/rest/alarms/action/acknowledge";
                isAck = true;
                break;
            case R.id.unAck:
                posturl = "/rest/alarms/action/unacknowledge";
                isAck = false;
                 break;
        }
        StringEntity entity = null;
        JSONArray jsonParams = new JSONArray();
        try {
            jsonParams.put(alarmObj.getId());
            entity = new StringEntity(jsonParams.toString());


        }  catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final boolean Acknowledge = isAck;

        AnutaRestClient.post(getApplicationContext(), posturl, entity, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "successfully done!", Toast.LENGTH_LONG).show();
                updateAlarmStatus(Acknowledge);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(getApplicationContext(), " Error occcured!", Toast.LENGTH_LONG).show();

            }
        });
        return super.onOptionsItemSelected(item);
    }
    private void updateAlarmStatus(boolean acknowledge)
    {
        alarmObj.setAcknowledged(acknowledge);
    }

}
