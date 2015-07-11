package com.anutanetworks.ncxapp.activity.approval;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.Approval;
import com.anutanetworks.ncxapp.model.SubUserTask;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Aakash on 7/6/2015.
 */
public class ApprovalActivity extends AppCompatActivity  {

    private String id;
    Approval approvalObj = new Approval();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        approvalObj = (Approval) i.getSerializableExtra("approvalObject");

        id = approvalObj.getId();
        setContentView(R.layout.activity_approval_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Approval Detail");
        actionBar.setDisplayUseLogoEnabled(false);

        getApprovalDetailData();
 }

    private void getApprovalDetailData() {
        AnutaRestClient.get("/rest/workflowtasks/" + id, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    ObjectMapper objectMapper1 = new ObjectMapper();
                    Object val1 = response.toString();
                    final Approval approvalData = objectMapper1.readValue(val1.toString(), Approval.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            updateApprovalDataEntries(approvalData);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(), "Unable to Load Approval Detail Data", Toast.LENGTH_LONG).show();
            }
        });
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approval, container, false);
        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_approval_detail, menu);
        return true;
    }
    String posturl = null;
    @Override
    public boolean onOptionsItemSelected(MenuItem approveMenu) {
       int menuid = approveMenu.getItemId();

        switch (approveMenu.getItemId())
        {
            case R.id.approved:
                posturl =  "/rest/workflowtasks/" + id + "/action/approve";
                Dialog approval = new Dialog(this);
                approval.setContentView(R.layout.prompt);
                approval.setTitle("Approve");
                Button canclebtn = (Button) approval.findViewById(R.id.cancel);
                EditText txtmsg = (EditText) approval.findViewById(R.id.msg);
                Button ok = (Button) approval.findViewById(R.id.okbtn);

                changeApprovedStatus(approval, canclebtn, txtmsg, ok);
                approval.show();
                break;

            case R.id.reject:
                posturl = "/rest/workflowtasks/" + id + "/action/reject";
                approval = new Dialog(this);
                approval.setContentView(R.layout.prompt);
                approval.setTitle("Reject");
                canclebtn = (Button) approval.findViewById(R.id.cancel);
                txtmsg = (EditText) approval.findViewById(R.id.msg);
                ok = (Button) approval.findViewById(R.id.okbtn);
                changeApprovedStatus(approval, canclebtn, txtmsg, ok);
               approval.show();
                break;
        }


        return super.onOptionsItemSelected(approveMenu);
    }

    private void changeApprovedStatus(final Dialog approval, Button canclebtn, final EditText txtmsg, Button ok) {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msgs = txtmsg.getText().toString();
                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    jsonParams.put("notes", msgs);
                    jsonParams.put("scheduleNow", true);
                    jsonParams.put("reScheduleTask", true);
                    entity = new StringEntity(jsonParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                AnutaRestClient.post(getApplicationContext(), posturl, entity, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "successfully done!", Toast.LENGTH_LONG).show();
                        approval.dismiss();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Toast.makeText(getApplicationContext(), " Error occcured!", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approval.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updateApprovalDataEntries(Approval approval) {
        TextView det = (TextView) findViewById(R.id.details);
        TextView comm = (TextView) findViewById(R.id.commands);

        List<SubUserTask> tasks = approval.getSubTasks();
        if (!tasks.isEmpty()) {
            SubUserTask task = tasks.get(0);
            det.setText(task.getDescription());
            comm.setText(task.getCommands());
        }


    }

}
