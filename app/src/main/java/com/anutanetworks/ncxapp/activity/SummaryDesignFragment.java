package com.anutanetworks.ncxapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.TasksSummary;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import java.io.IOException;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aakash on 7/7/2015.
 */
public class SummaryDesignFragment extends Fragment {


   View view = null;

   @Override public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }

   @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
      view =  inflater.inflate(R.layout.fragment_system_summary, container, false);

      AnutaRestClient.get("/rest/tasks/summary", null, new JsonHttpResponseHandler() {
         @Override public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
               ObjectMapper objectMapper = new ObjectMapper();
               final TasksSummary tasksSummary = objectMapper.readValue(response.toString(), TasksSummary.class);
               getActivity().runOnUiThread(new Runnable() {
                  @Override public void run() {
                     addData(tasksSummary);
                  }
               });

            } catch (IOException e) {
               e.printStackTrace();
            }
         }

         @Override public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
         }

         @Override public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
         }
      });
      return view;
   }

   private void addData(TasksSummary tasksSummary) {
      TextView tenantCount = (TextView) view.findViewById(R.id.tenantCountText);
      tenantCount.setText(String.valueOf(tasksSummary.getTenants()));
      TextView vdcCountText = (TextView) view.findViewById(R.id.vdcCountText);
      vdcCountText.setText(String.valueOf(tasksSummary.getVdcs()));
      TextView vmsCountText = (TextView) view.findViewById(R.id.vmsCountText);
      vmsCountText.setText(String.valueOf(tasksSummary.getVms()));
      TextView runningTasksText = (TextView) view.findViewById(R.id.runningTaskCountText);
      runningTasksText.setText(String.valueOf(tasksSummary.getTasksRunning()));
      TextView waitingTasksText = (TextView) view.findViewById(R.id.waitingTaskCountText);
      waitingTasksText.setText(String.valueOf(tasksSummary.getTasksWaiting()));
      TextView errorTasksText = (TextView) view.findViewById(R.id.errorTaskCountText);
      errorTasksText.setText(String.valueOf(tasksSummary.getTasksError()));
   }

}
