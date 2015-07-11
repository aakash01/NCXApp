package com.anutanetworks.ncxapp.activity.dashboard;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.Capacity;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/6/2015.
 */
public class DashboardServicesSummaryFragment extends Fragment {

    private BarChart mChart;

    private List<Capacity> capacitylist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_summary, container, false);
        mChart = (BarChart) view.findViewById(R.id.serviceSummaryChart);
        mChart.setDescription("");
        getSummaryData();
        return view;
    }
    private void getSummaryData()

    {
        AnutaRestClient.get("/rest/capacities/filter/top?componentType=SERVICE&capacityType&count=0", null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            final ArrayList<Capacity> capacities = objectMapper
                                    .readValue(response.toString(), new TypeReference<List<Capacity>>() {
                                    });
                            Activity activity = getActivity();
                            if (null == activity) {
                                return;
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addData(capacities);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getActivity(), "Unable to Load Service Summary Data", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addData(List<Capacity> capacities) {
        capacitylist = capacities;
        ArrayList<BarEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        for (int i = 0; i < capacities.size(); i++) {
            Capacity capacity = capacities.get(i);
            yEntry.add(new BarEntry(capacity.getUsed(), i));
            xEntry.add(capacity.getComponentName());
        }
        BarDataSet dataSet = new BarDataSet(yEntry, "Services-Top 5 in Utilization");

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        colors.add(Color.rgb(0, 155, 0));
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        BarData barData = new BarData(xEntry, dataSet);
        mChart.setData(barData);
        mChart.animateXY(2000, 2000);
        mChart.invalidate();
        mChart.notifyDataSetChanged();
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
    }

}
