package com.anutanetworks.ncxapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.AlarmsSummary;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Aakash on 7/8/2015.
 */
public class AlarmSummaryFragment extends Fragment {


    View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alarm_summary, container, false);

        AnutaRestClient.get("/rest/alarms/summary", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    final AlarmsSummary alarmsSummary = objectMapper.readValue(response.toString(), AlarmsSummary.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addData(alarmsSummary);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        return view;
    }

    private void addData(AlarmsSummary alarmsSummary) {
        TextView alarmCritical = (TextView) view.findViewById(R.id.alarmCritical);
        alarmCritical.setText(String.valueOf(alarmsSummary.getCritical()));
        TextView alarmWarning = (TextView) view.findViewById(R.id.alarmWarning);
        alarmWarning.setText(String.valueOf(alarmsSummary.getWarning()));
        TextView alarmMajor = (TextView) view.findViewById(R.id.alarmMajor);
        alarmMajor.setText(String.valueOf(alarmsSummary.getMajor()));
        TextView alarmMinor = (TextView) view.findViewById(R.id.alarmMinor);
        alarmMinor.setText(String.valueOf(alarmsSummary.getMinor()));
        TextView alarminfo = (TextView) view.findViewById(R.id.alarminfo);
        alarminfo.setText(String.valueOf(alarmsSummary.getInfo()));
    }
}