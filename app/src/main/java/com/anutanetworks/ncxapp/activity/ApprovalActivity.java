package com.anutanetworks.ncxapp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aakash on 7/6/2015.
 */
public class ApprovalActivity extends Activity implements View.OnClickListener {
   private String id;
    Button acceptb;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        setContentView(R.layout.activity_approval_grid);

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
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        acceptb = (Button) findViewById(R.id.acceptbtn);
             acceptb.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


            final Dialog approval = new Dialog(this);
            approval.setContentView(R.layout.prompt);
            approval.setTitle("Approve");
            final  Button canclebtn = (Button)approval.findViewById(R.id.cancel);
            final EditText txtmsg = (EditText)approval.findViewById(R.id.msg);
            final Button  ok = (Button)approval.findViewById(R.id.okbtn);

             ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   String msgs = txtmsg.getText().toString();
                   JSONObject jsonParams = new JSONObject();
                   StringEntity entity = null;
                   try {
                      jsonParams.put("notes", msgs);
                      jsonParams.put("scheduleNow",true);
                      jsonParams.put("reScheduleTask",true);
                      entity = new StringEntity(jsonParams.toString());
                   } catch (JSONException e) {
                      e.printStackTrace();
                   } catch (UnsupportedEncodingException e) {
                      e.printStackTrace();
                   }

                   AnutaRestClient.post(getApplicationContext(),"/rest/workflowtasks/"+id+"/action/approve", entity, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(getApplicationContext(), "successfully approved!", Toast.LENGTH_LONG).show();
                            approval.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            Toast.makeText(getApplicationContext(), " Error occcured!", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            });


            /* btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtUsername.getText().toString().trim().length() > 0 && txtPassword.getText().toString().trim().length() > 0)
                    {
                        // Validate Your login credential here than display message
                        Toast.makeText(Pulse7LoginDialogActivity.this,
                                "Login Sucessfull", Toast.LENGTH_LONG).show();

                        // Redirect to dashboard / home screen.
                        login.dismiss();
                    }
                    else
                    {
                        Toast.makeText(Pulse7LoginDialogActivity.this,
                                "Please enter Username and Password", Toast.LENGTH_LONG).show();

                    }
                }
            });*/
       canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approval.dismiss();
            }
            });


            approval.show();
        }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approval, container, false);
        return view;
    }


          @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void  updateApprovalDataEntries(Approval approval){
        TextView det = (TextView)findViewById(R.id.details);
        TextView comm=(TextView)findViewById(R.id.commands);

        List<SubUserTask> tasks = approval.getSubTasks();
        if(!tasks.isEmpty()){
            SubUserTask task = tasks.get(0);
            det.setText(task.getDescription());
            comm.setText(task.getCommands());
        }



    }

        }
