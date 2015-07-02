package com.anutanetworks.ncxapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.services.ValidationUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aakash on 7/2/2015.
 */
public class LoginActivity extends Activity {

    ProgressDialog progressDialog;

    TextView errorMsg;
    EditText hostUrlText;
    EditText usernameText;
    EditText passwordText;
    EditText organizationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorMsg = (TextView)findViewById(R.id.login_error);
        hostUrlText = (EditText)findViewById(R.id.login_hosturl);
        usernameText = (EditText)findViewById(R.id.login_username);
        passwordText = (EditText)findViewById(R.id.login_password);
        organizationText = (EditText)findViewById(R.id.login_organization);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating babua...");
        progressDialog.setCancelable(false);
    }

    public void loginUser(View view){
        String host_url = hostUrlText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String organization = organizationText.getText().toString();
        Map<String,String> paramMap = new HashMap();
        if(ValidationUtils.isNotNull(host_url)) {
            paramMap.put("host_url",host_url);
            if (ValidationUtils.isNotNull(username) && ValidationUtils.isNotNull(password)) {
                paramMap.put("username", username);
                paramMap.put("password", password);
                paramMap.put("organization", organization);
                validateLogin(paramMap);
            } else {
                Toast.makeText(getApplicationContext(), "Please fill username and password", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please specify ncx host url.", Toast.LENGTH_LONG).show();
        }
    }

    public void validateLogin(Map<String, String> paramMap){
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        String baseUrl = paramMap.get("host_url");
        String actualUsername = paramMap.get("username");
        String password = paramMap.get("password");
        String organization = paramMap.get("organization");

        if(ValidationUtils.isNotNull(organization)){
            actualUsername += "|" + organization;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.add("AnutaAPIVersion","2.0");
        client.setBasicAuth(actualUsername, password);

        client.post(baseUrl + "/login", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                navigatetoMainActivity();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.hide();

                if(statusCode == 401){
                    errorMsg.setText("Invalid username / password");
                    Toast.makeText(getApplicationContext(), "Authentication Failure", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void navigatetoMainActivity(){
        Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
    }

}
