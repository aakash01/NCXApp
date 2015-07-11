package com.anutanetworks.ncxapp.activity.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.AlarmsSummary;
import com.anutanetworks.ncxapp.model.TasksSummary;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Aakash on 7/7/2015.
 */
public class DashboardSystemSummaryFragment extends Fragment {


    View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_system_summary, container, false);

        getSystemSummaryData();
        getAlarmSummaryData();
        return view;
    }

    private void getAlarmSummaryData() {
        AnutaRestClient.get("/rest/alarms/summary", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    final AlarmsSummary alarmsSummary = objectMapper.readValue(response.toString(), AlarmsSummary.class);
                    Activity activity = getActivity();
                    if(activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateAlarmSummaryData(alarmsSummary);
                            }
                        });
                    }
               } catch (IOException e) {
                    e.printStackTrace();
                }
            }
             @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                 Toast.makeText(getActivity(), "Unable to Load Alarm Summary Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSystemSummaryData() {
        AnutaRestClient.get("/rest/tasks/summary", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    final TasksSummary tasksSummary = objectMapper.readValue(response.toString(), TasksSummary.class);
                    Activity activity = getActivity();
                    if(activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateSystemSummaryData(tasksSummary);
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), "Unable to Load System Summary Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateSystemSummaryData(TasksSummary tasksSummary) {
        TextView tenantCount = (TextView) view.findViewById(R.id.summarytenant);
        tenantCount.setText(String.valueOf(tasksSummary.getTenants()));
        TextView vdcCountText = (TextView) view.findViewById(R.id.summaryvdc);
        vdcCountText.setText(String.valueOf(tasksSummary.getVdcs()));
        TextView vmsCountText = (TextView) view.findViewById(R.id.summaryVms);
        vmsCountText.setText(String.valueOf(tasksSummary.getVms()));
        TextView runningTasksText = (TextView) view.findViewById(R.id.summaryRunning);
        runningTasksText.setText(String.valueOf(tasksSummary.getTasksRunning()));
        TextView waitingTasksText = (TextView) view.findViewById(R.id.summaryWaiting);
        waitingTasksText.setText(String.valueOf(tasksSummary.getTasksWaiting()));
        TextView errorTasksText = (TextView) view.findViewById(R.id.summaryError);
        errorTasksText.setText(String.valueOf(tasksSummary.getTasksError()));
    }


    private void updateAlarmSummaryData(AlarmsSummary alarmsSummary) {
        TextView alarmCritical = (TextView) view.findViewById(R.id.alarmCritical);
        alarmCritical.setText(String.valueOf(alarmsSummary.getCritical()));
        TextView alarmWarning = (TextView) view.findViewById(R.id.alarmWarning);
        alarmWarning.setText(String.valueOf(alarmsSummary.getWarning()));
        TextView alarmMajor = (TextView) view.findViewById(R.id.alarmMajor);
        alarmMajor.setText(String.valueOf(alarmsSummary.getMajor()));
        TextView alarmMinor = (TextView) view.findViewById(R.id.alarmMinor);
        alarmMinor.setText(String.valueOf(alarmsSummary.getMinor()));
        TextView alarminfo = (TextView) view.findViewById(R.id.alarmInfo);
        alarminfo.setText(String.valueOf(alarmsSummary.getInfo()));
    }

}
