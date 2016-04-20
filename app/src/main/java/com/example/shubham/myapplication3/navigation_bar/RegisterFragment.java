package com.example.shubham.myapplication3.navigation_bar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
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
import com.example.shubham.myapplication3.NotificationView;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;
import com.example.shubham.myapplication3.register.SignUp;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shubham on 30-09-2015.
 */
public class RegisterFragment extends Fragment {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 100;
    public ProgressDialog pDialog;
    EditText email,password;
    Button loginbutton;
    TextView signuplink,noti_name;
    public RegisterFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.log_in, container, false);
        email = (EditText) rootView.findViewById(R.id.input_email);
        password = (EditText) rootView.findViewById(R.id.input_password);
        loginbutton = (Button) rootView.findViewById(R.id.btn_login);
        signuplink = (TextView) rootView.findViewById(R.id.link_signup);
        noti_name = (TextView) rootView.findViewById(R.id.textView16);
        loginbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

            signuplink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getActivity(), SignUp.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });

        return rootView;

    }
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            //onLoginFailed();
            return;
        }
        loginbutton.setEnabled(false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Authenticating...");
        pDialog.show();

        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        //final String url = "http://192.168.173.253/laravel/public/applogin";
        final String url = "http://172.16.110.69/laravel/public/applogin";
        HashMap<String,String> params =new HashMap<String,String >();
        params.put("email",email);
        params.put("password", password);
        //show progressdialog for some time
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        pDialog.dismiss();
                    }
                }, 1000);
        //request and response
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.dismiss();
                        try {
                            String ress= response.getString("msg");
                            Boolean suc=response.getBoolean("success");
                            Toast.makeText(getActivity(), ress, Toast.LENGTH_SHORT).show();
                            String user = response.getString("email");
                            loginbutton.setEnabled(true);
                            SharedPreferences pref = getActivity().getSharedPreferences("USERID",0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                            editor.commit();
                            if(response.getBoolean("success")){
                                noti_name.setText(response.getString("name"));
                                editor.putString("user",response.getString("email"));
                                editor.commit();
                                onLoginSuccess();
                            }
                            else {
                                editor.putString("user",null);
                                editor.commit();
                                onLoginFailed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),"Check Your Network Connection",Toast.LENGTH_SHORT);
                onLoginFailed();
            }
        });
        AppController.getInstance().addToRequestQueue(req);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {

                // TODO: Implement successful signup logic here
                String s_email = data.getStringExtra("s_email");
                String s_password = data.getStringExtra("s_password");
                email.setText(s_email);
                password.setText(s_password);
            }

        }

    }

//
//    public void onBackPressed() {
//        // disable going back to the MainActivity
////        Intent intent = new Intent(getActivity(), MainNavigation.class);
////        startActivity(intent);
////        getActivity().finish();
//    }

    public void onLoginSuccess() {
//        SharedPreferences pref = getActivity().getSharedPreferences("USERID", 0);
//        String user_email = pref.getString("user", null);
       // getUserName(user_email);

        loginbutton.setEnabled(true);
        String msg= "Hi "+(noti_name.getText());
        Notify(msg, "Welcome to WGA");
        Intent intent = new Intent(getActivity(), MainNavigation.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void onLoginFailed() {
        loginbutton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

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

    private void Notify(String notificationTitle, String notificationMessage){
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

        Notification notification = new Notification(R.drawable.icon,"New Message", System.currentTimeMillis());
        Intent notificationIntent = new Intent(getActivity(),NotificationView.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0,notificationIntent, 0);

        notification.defaults |= Notification.DEFAULT_ALL;

//        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(getActivity());
//        mBuilder.setSound(Uri.EMPTY);
        notification.setLatestEventInfo(getActivity(), notificationTitle,notificationMessage, pendingIntent);
        notificationManager.notify(9999, notification);
    }



}
