package com.example.shubham.myapplication3.register;

/**
 * Created by Shubham on 02-10-2015.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;
import com.example.shubham.myapplication3.location.GPSTracker;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUp extends ActionBarActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name)
    EditText name;
    @InjectView(R.id.input_email)
    EditText email;
    @InjectView(R.id.input_password)
    EditText password;
    @InjectView(R.id.btn_signup)
    Button signupbutton;
    @InjectView(R.id.link_login)
    TextView loginlink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        //back to login page
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            //onSignupFailed();
            return;
        }

        signupbutton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        final String url = "http://172.16.110.69/laravel/public/appsignup";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);

        //progress dialog
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);



        //request and response
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        progressDialog.dismiss();
                        try {
                            Toast.makeText(SignUp.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(SignUp.this,"fffffff",Toast.LENGTH_SHORT).show();
                            if (response.getBoolean("success")) {
                                onSignupSuccess();
                            } else {
                                onSignupFailed();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(SignUp.this, "Check Your Network Connection", Toast.LENGTH_SHORT);
                onSignupFailed();
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }


    public void onSignupSuccess() {
        signupbutton.setEnabled(true);
        Intent intent = new Intent();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        intent.putExtra("s_email", email);
        intent.putExtra("s_password", password);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onSignupFailed() {
        signupbutton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            this.name.setError("at least 3 characters");
            valid = false;
        } else {
            this.name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("enter a valid email address");
            valid = false;
        } else {
            this.email.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            this.password.setError("at least 8 alphanumeric characters");
            valid = false;
        } else {
            this.password.setError(null);
        }

        return valid;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home)
            finish();
            return super.onOptionsItemSelected(item);
    }

}