package com.example.shubham.myapplication3.navigation_bar;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shubham.myapplication3.MainNavigation;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubham on 17-11-2015.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "Profile Fragment";
    private String user_email;
    private TextView user_name,email;
    private ProgressDialog pdialog;
    private String oldP,retypeP,newP;
    private EditText old_password,new_password,retype_password;
    private Button submit,change_password;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile, container, false);

        final SharedPreferences pref = getActivity().getSharedPreferences("USERID", 0);
        user_email= pref.getString("user", null);
        user_name = (TextView) rootView.findViewById(R.id.user_name);
        email = (TextView) rootView.findViewById(R.id.user_email);

        getUserName();

        change_password = (Button) rootView.findViewById(R.id.change_password);
        old_password = (EditText) rootView.findViewById(R.id.old_password);
        new_password = (EditText) rootView.findViewById(R.id.new_password);
        retype_password = (EditText) rootView.findViewById(R.id.retype_password);
        submit = (Button) rootView.findViewById(R.id.submit);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_password.setEnabled(false);
                old_password.setVisibility(View.VISIBLE);
                new_password.setVisibility(View.VISIBLE);
                retype_password.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        final Button logOut = (Button) rootView.findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getActivity(), "Successfully LogOut", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainNavigation.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return rootView;
    }

    private void changePassword(){
        oldP = old_password.getText().toString();
        newP = new_password.getText().toString();
        retypeP = retype_password.getText().toString();
        if (!validate(oldP,newP,retypeP)) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        final String url = "http://172.16.110.69/laravel/public/appChangePassword";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("oldP", oldP);
        params.put("newP", newP);
        params.put("email",user_email);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        progressDialog.dismiss();
                        try {
                            Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                            if (response.getBoolean("success")) {
                                old_password.setError(null);
                                change_password.setEnabled(true);
                                old_password.setVisibility(View.GONE);
                                new_password.setVisibility(View.GONE);
                                retype_password.setVisibility(View.GONE);
                                submit.setVisibility(View.GONE);
                            }
                            else
                                old_password.setError("Incorrect");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Check Your Network Connection", Toast.LENGTH_SHORT);
            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }

    private void getUserName(){
        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Loading..");
        pdialog.show();
        final String url = "http://weekendaway.esy.es/appProfile";
        Map<String ,String> params = new HashMap<String,String>();
        params.put("email", user_email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pdialog.dismiss();
                        try {
                            user_name.setText(response.getString("name"));
                            email.setText(response.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    public boolean validate(String oldP, String newP, String retypeP) {
        boolean valid = true;

        if (oldP.isEmpty() || oldP.length() < 8) {
            this.old_password.setError("at least 8 alphanumeric characters");
            valid = false;
        } else {
            this.old_password.setError(null);
        }

        if (newP.isEmpty() || newP.length() < 8) {
            this.new_password.setError("at least 8 alphanumeric characters");
            valid = false;
        } else {
            this.new_password.setError(null);
        }

        if (retypeP.isEmpty() || !retypeP.equals(newP)) {
            this.retype_password.setError("Not Match");
            valid = false;
        } else {
            this.retype_password.setError(null);
        }
        return valid;
    }

}
