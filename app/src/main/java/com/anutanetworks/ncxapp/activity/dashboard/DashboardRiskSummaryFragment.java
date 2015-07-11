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
import com.anutanetworks.ncxapp.model.Capacity;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/8/2015.
 */
public class DashboardRiskSummaryFragment extends Fragment {

    View view = null;
    private List<Capacity> capacitylists;
    private List<Capacity> healthlist;
    private List<Capacity> worklist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_risk_summary, container, false);




        getRiskHealthData();
        getRiskCapacityData();
        getRiskWorkloadData();

        return view;
    }



    private void getRiskHealthData() {
        AnutaRestClient.get("/rest/capacities/filter/top?componentType=SUBNETWORK&capacityType=AGGREGATE_HEALTH&count=5", null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            final ArrayList<Capacity> healths = objectMapper
                                    .readValue(response.toString(), new TypeReference<List<Capacity>>() {
                                    });
                            Activity activity = getActivity();
                            if (activity != null) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        addhealth(healths);
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
                        Toast.makeText(getActivity(), "Unable to Load At Risk Pods By Health Data", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void getRiskCapacityData() {
        AnutaRestClient.get("/rest/capacities/filter/top?componentType=SUBNETWORK&capacityType=AGGREGATE_CAPACITY&count=5", null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            final ArrayList<Capacity> capacities = objectMapper
                                    .readValue(response.toString(), new TypeReference<List<Capacity>>() {
                                    });
                            Activity activity = getActivity();
                            if (activity != null) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        addData(capacities);
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
                        Toast.makeText(getActivity(), "Unable to Load At Risk Pods By Utilized Capacity Data", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getRiskWorkloadData() {
        AnutaRestClient.get("/rest/capacities/filter/top?componentType=SUBNETWORK&capacityType=AGGREGATE_WORKLOAD&count=5", null,
                new JsonHttpResponseHandler() {
                   @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            final ArrayList<Capacity> works = objectMapper
                                    .readValue(response.toString(), new TypeReference<List<Capacity>>() {
                                    });
                            Activity activity = getActivity();
                            if (activity != null) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        addwork(works);
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
                        Toast.makeText(getActivity(), "Unable to Load At Risk Pods By Workload Data", Toast.LENGTH_LONG).show();
                    }
                });
    }



    private void addData(List<Capacity> capacities) {
        capacitylists = capacities;
        Capacity capacity = capacities.get(0);
        TextView util1 = (TextView) view.findViewById(R.id.util1);
        util1.setText(String.valueOf(capacity.getUsed()));
        TextView capacitytxt1 = (TextView) view.findViewById(R.id.capacitytxt1);
        capacitytxt1.setText(String.valueOf(capacity.getComponentName()));

        Capacity capacity2 = capacities.get(1);
        TextView util2 = (TextView) view.findViewById(R.id.util2);
        util2.setText(String.valueOf(capacity2.getUsed()));
        TextView capacitytxt2 = (TextView) view.findViewById(R.id.capacitytxt2);
        capacitytxt2.setText(String.valueOf(capacity2.getComponentName()));

        Capacity capacity3 = capacities.get(2);
        TextView util3 = (TextView) view.findViewById(R.id.util3);
        util3.setText(String.valueOf(capacity3.getUsed()));
        TextView capacitytxt3 = (TextView) view.findViewById(R.id.capacitytxt3);
        capacitytxt3.setText(String.valueOf(capacity3.getComponentName()));

        Capacity capacity4 = capacities.get(3);
        TextView util4 = (TextView) view.findViewById(R.id.util4);
        util4.setText(String.valueOf(capacity4.getUsed()));
        TextView capacitytxt4 = (TextView) view.findViewById(R.id.capacitytxt4);
        capacitytxt4.setText(String.valueOf(capacity4.getComponentName()));

        Capacity capacity5 = capacities.get(4);
        TextView util5 = (TextView) view.findViewById(R.id.util5);
        util5.setText(String.valueOf(capacity5.getUsed()));
        TextView capacitytxt5 = (TextView) view.findViewById(R.id.capacitytxt5);
        capacitytxt5.setText(String.valueOf(capacity5.getComponentName()));


    }

    private void addhealth(List<Capacity> healths) {
        healthlist = healths;
        Capacity capacityh = healths.get(0);
        TextView health1 = (TextView) view.findViewById(R.id.health1);
        health1.setText(String.valueOf(capacityh.getUsed()));
        TextView healthtxt1 = (TextView) view.findViewById(R.id.healthtxt1);
        healthtxt1.setText(String.valueOf(capacityh.getComponentName()));

        Capacity capacityh2 = healths.get(1);
        TextView health2 = (TextView) view.findViewById(R.id.health2);
        health2.setText(String.valueOf(capacityh2.getUsed()));
        TextView healthtxt2 = (TextView) view.findViewById(R.id.healthtxt2);
        healthtxt2.setText(String.valueOf(capacityh2.getComponentName()));

        Capacity capacityh3 = healths.get(2);
        TextView health3 = (TextView) view.findViewById(R.id.health3);
        health3.setText(String.valueOf(capacityh3.getUsed()));
        TextView healthtxt3 = (TextView) view.findViewById(R.id.healthtxt3);
        healthtxt3.setText(String.valueOf(capacityh3.getComponentName()));

        Capacity capacityh4 = healths.get(3);
        TextView health4 = (TextView) view.findViewById(R.id.health4);
        health4.setText(String.valueOf(capacityh4.getUsed()));
        TextView healthtxt4 = (TextView) view.findViewById(R.id.healthtxt4);
        healthtxt4.setText(String.valueOf(capacityh4.getComponentName()));

        Capacity capacityh5 = healths.get(4);
        TextView health5 = (TextView) view.findViewById(R.id.health5);
        health5.setText(String.valueOf(capacityh5.getUsed()));
        TextView healthtxt5 = (TextView) view.findViewById(R.id.healthtxt5);
        healthtxt5.setText(String.valueOf(capacityh5.getComponentName()));


    }


    private void addwork(List<Capacity> works) {
        worklist = works;
        Capacity capacityw = works.get(0);
        TextView work1 = (TextView) view.findViewById(R.id.work1);
        work1.setText(String.valueOf(capacityw.getUsed()));
        TextView worktxt1 = (TextView) view.findViewById(R.id.worktxt1);
        worktxt1.setText(String.valueOf(capacityw.getComponentName()));

        Capacity capacityw2 = works.get(1);
        TextView work2 = (TextView) view.findViewById(R.id.work2);
        work2.setText(String.valueOf(capacityw2.getUsed()));
        TextView worktxt2 = (TextView) view.findViewById(R.id.worktxt2);
        worktxt2.setText(String.valueOf(capacityw2.getComponentName()));

        Capacity capacityw3 = works.get(2);
        TextView work3 = (TextView) view.findViewById(R.id.work3);
        work3.setText(String.valueOf(capacityw3.getUsed()));
        TextView worktxt3 = (TextView) view.findViewById(R.id.worktxt3);
        worktxt3.setText(String.valueOf(capacityw3.getComponentName()));

        Capacity capacityw4 = works.get(3);
        TextView work4 = (TextView) view.findViewById(R.id.work4);
        work4.setText(String.valueOf(capacityw4.getUsed()));
        TextView worktxt4 = (TextView) view.findViewById(R.id.worktxt4);
        worktxt4.setText(String.valueOf(capacityw4.getComponentName()));

        Capacity capacityw5 = works.get(4);
        TextView work5 = (TextView) view.findViewById(R.id.work5);
        work5.setText(String.valueOf(capacityw5.getUsed()));
        TextView worktxt5 = (TextView) view.findViewById(R.id.worktxt5);
        worktxt5.setText(String.valueOf(capacityw5.getComponentName()));


    }


}

