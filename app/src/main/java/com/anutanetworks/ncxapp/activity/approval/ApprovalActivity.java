package com.anutanetworks.ncxapp.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.adapter.ApprovalDetailActivityAdapter;
import com.anutanetworks.ncxapp.model.Approval;
import com.anutanetworks.ncxapp.model.ApprovalDetailItem;
import com.anutanetworks.ncxapp.model.SubUserTask;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.anutanetworks.ncxapp.services.SampleDataGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/6/2015.
 */
public class ApprovalActivity extends AppCompatActivity  {

    private String id;
    Approval approvalObj = new Approval();

    private ApprovalDetailActivityAdapter commandsAdapter;
    private AbsListView mListView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        approvalObj = (Approval) i.getSerializableExtra("approvalObject");

        id = approvalObj.getId();
        setContentView(R.layout.activity_approval_detail_list);
        commandsAdapter = new ApprovalDetailActivityAdapter(getApplicationContext(), new ArrayList<ApprovalDetailItem>());
        mListView = (AbsListView) findViewById(R.id.commandList);
        ((AdapterView<ListAdapter>) mListView).setAdapter(commandsAdapter);

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
        if(AnutaRestClient.isAllowOffline()){
            updateApprovalDataEntries(SampleDataGenerator.getApprovalDetail());
        } else {
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
    }
      @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean approve = false;
        if("Approved".equals(approvalObj.getApproved()) || "Rejected".equals(approvalObj.getApproved())) {
            approve = true;
        }
        getMenuInflater().inflate(R.menu.menu_approval_detail, menu);
        if(approve) {
            for(int i=0; i<menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }
    String posturl = null;

    @Override
    public boolean onOptionsItemSelected(MenuItem approveMenu) {
        switch (approveMenu.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.approved:
                posturl =  "/rest/workflowtasks/" + id + "/action/approve";

                new MaterialDialog.Builder(this)
                        .title("Approve")
                        .content("Notes")
                        .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        .input(null,null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                approveOrReject(input.toString());
                            }
                        }).negativeText("Cancel").show();
                break;
            case R.id.reject:
                posturl = "/rest/workflowtasks/" + id + "/action/reject";
                new MaterialDialog.Builder(this)
                        .title("Reject")
                        .content("Notes")
                        .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        .input(null,null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                approveOrReject(input.toString());
                            }
                        }).negativeText("Cancel").show();
                break;
        }

        return super.onOptionsItemSelected(approveMenu);
    }

    private void approveOrReject(String notes) {
                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    jsonParams.put("notes", notes);
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

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("internal-error",error.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), " Error occcured!", Toast.LENGTH_LONG).show();

                    }
                });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updateApprovalDataEntries(Approval approval) {
        List<SubUserTask> tasks = approval.getSubTasks();
        List<ApprovalDetailItem> listData = new ArrayList<>();
        if (!tasks.isEmpty()) {
            SubUserTask task = tasks.get(0);
            ApprovalDetailItem detailHeader = new ApprovalDetailItem();
            detailHeader.setHeader(true);
            detailHeader.setValue("Details");
            listData.add(detailHeader);

            String detailString = "";
            for(String s : task.getDescription().split(";")){
                detailString +=s + "\n";
            }

            ApprovalDetailItem detailData = new ApprovalDetailItem();
            detailData.setDetailData(true);
            detailData.setValue(detailString);
            listData.add(detailData);

            ApprovalDetailItem commandHeader = new ApprovalDetailItem();
            commandHeader.setHeader(true);
            commandHeader.setValue("Commands");
            listData.add(commandHeader);

            String[] commands  = task.getCommands().split("(?=\\b(?:(DEVICE(.*)\\))))");

            for(String command : commands){
                if(command.contains("Result")){
                    continue;
                }
                String deviceName = command.substring(0,command.indexOf("\n"));
                String commandItems = command.substring(command.indexOf("\n")+1);

                ApprovalDetailItem device = new ApprovalDetailItem();
                device.setDevice(true);
                device.setValue(deviceName);
                listData.add(device);

                ApprovalDetailItem commandData = new ApprovalDetailItem();
                commandData.setCommandData(true);
                commandData.setValue(commandItems);
                listData.add(commandData);
            }
        }
        commandsAdapter.updateItems(listData);
    }

}
