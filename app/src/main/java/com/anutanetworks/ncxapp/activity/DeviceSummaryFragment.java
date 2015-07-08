package com.anutanetworks.ncxapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.Capacity;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.loopj.android.http.JsonHttpResponseHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aakash on 7/6/2015.
 */
public class DeviceSummaryFragment extends Fragment {

    private PieChart mChart;

    private List<Capacity> capacitylist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_device_summary, container, false);
        mChart = (PieChart) view.findViewById(R.id.chart);
        mChart.setUsePercentValues(true);
        mChart.setDescription("Device Summary By Vendor");
       mChart.setDragDecelerationFrictionCoef(0.95f);

       mChart.setDrawHoleEnabled(true);
       mChart.setHoleColorTransparent(true);
       mChart.setTransparentCircleColor(Color.WHITE);

       mChart.setHoleRadius(58f);
       mChart.setTransparentCircleRadius(61f);

       mChart.setDrawCenterText(true);
       mChart.setDrawSliceText(false);

       mChart.setRotationAngle(0);
       // enable rotation of the chart by touch
       mChart.setRotationEnabled(true);

       mChart.setNoDataText("No Data Available");

       // add a selection listener
       mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
           @Override public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
              if (e == null) {
                 return;
              }
              mChart.setCenterText(capacitylist.get(e.getXIndex()).getComponentName() + "\n"+ e.getVal());
              mChart.setCenterTextSize(20f);
           }

           @Override public void onNothingSelected() {

           }
        });
        AnutaRestClient.get("/rest/devices/summary/filter/vendor", null, new JsonHttpResponseHandler() {
           @Override public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
           }

           @Override public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
              try {
                 ObjectMapper objectMapper = new ObjectMapper();
                 final ArrayList<Capacity> capacities = objectMapper
                                .readValue(response.toString(), new TypeReference<List<Capacity>>() {
                                });
                 getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                       addData(capacities);
                    }
                 });

              } catch (IOException e) {
                 e.printStackTrace();
              }
           }

           @Override public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                          JSONObject errorResponse) {
              super.onFailure(statusCode, headers, throwable, errorResponse);
           }
        });
        return view;
    }

    private void addData(List<Capacity> capacities) {
       capacitylist = capacities;
        ArrayList<Entry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
for(int i=0;i<capacities.size();i++){
    Capacity capacity = capacities.get(i);
    yEntry.add(new Entry(capacity.getUsed(),i));
    xEntry.add(capacity.getComponentName());
}
        PieDataSet dataSet = new PieDataSet(yEntry, "Device Summary");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<>();

        for(int c: ColorTemplate.COLORFUL_COLORS){
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData pieData = new PieData(xEntry,dataSet);
        pieData.setValueFormatter(new PercentFormatter());
       pieData.setValueTextSize(11f);
       pieData.setValueTextColor(Color.GRAY);
        mChart.setData(pieData);
       mChart.highlightValues(null);

        mChart.invalidate();
       mChart.notifyDataSetChanged();
       Legend l = mChart.getLegend();
       l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
       l.setXEntrySpace(7f);
       l.setYEntrySpace(0f);
       l.setYOffset(0f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
