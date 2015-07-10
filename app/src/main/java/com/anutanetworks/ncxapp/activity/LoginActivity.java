package com.anutanetworks.ncxapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.anutanetworks.ncxapp.services.ValidationUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aakash on 7/2/2015.
 */
public class LoginActivity extends Activity {

    public static final String PREFS_NAME = "LoginPrefs";
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


        errorMsg = (TextView) findViewById(R.id.login_error);
        hostUrlText = (EditText) findViewById(R.id.login_hosturl);
        usernameText = (EditText) findViewById(R.id.login_username);
        passwordText = (EditText) findViewById(R.id.login_password);
        organizationText = (EditText) findViewById(R.id.login_organization);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating babua...");
        progressDialog.setCancelable(false);
    }

    public void loginUser(View view) {
        String host_url = hostUrlText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String organization = organizationText.getText().toString();
        Map<String, String> paramMap = new HashMap();
        if (ValidationUtils.isNotNull(host_url)) {
            paramMap.put("host_url", host_url);
            if (ValidationUtils.isNotNull(username) && ValidationUtils.isNotNull(password)) {
                paramMap.put("username", username);
                paramMap.put("password", password);
                paramMap.put("organization", organization);
                validateLogin(paramMap);
            } else {
                Toast.makeText(getApplicationContext(), "Please fill username and password", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please specify ncx host url.", Toast.LENGTH_LONG).show();
        }
    }

    public void validateLogin(final Map<String, String> paramMap) {
        progressDialog.show();
        AnutaRestClient.initializeClient(paramMap);

        AnutaRestClient.post("/login", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                navigatetoMainActivity(paramMap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.hide();
                if (statusCode == 401) {
                    errorMsg.setText("Invalid username / password");
                    Toast.makeText(getApplicationContext(), "Authentication Failure", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void navigatetoMainActivity(Map<String, String> paramMap) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        for (String key : paramMap.keySet()) {
            editor.putString(key, paramMap.get(key));
        }
        editor.putString("logged", "logged");
        editor.commit();

        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
    }

}
