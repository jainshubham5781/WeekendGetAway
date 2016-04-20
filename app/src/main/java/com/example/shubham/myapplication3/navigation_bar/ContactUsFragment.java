package com.example.shubham.myapplication3.navigation_bar;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubham on 11-10-2015.
 */
public class ContactUsFragment extends Fragment {

    private static final String TAG = "ContactUsFragment";
    public ProgressDialog pdialog;
    EditText comment;
    RatingBar ratingBar;
    Button add_review;
    String user_email,title;
    TextView movie_name;
    LinearLayout llout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_add_reviews, container, false);

        llout = (LinearLayout) rootView.findViewById(R.id.llout);
        llout.setBackgroundColor(Color.parseColor("#7eccf3"));
        llout.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);

        final SharedPreferences pref = getActivity().getSharedPreferences("USERID", 0);
        user_email = pref.getString("user", null);

        movie_name = (TextView) rootView.findViewById(R.id.textView);
        //movie_name.setText("Write Something :-");
        movie_name.setText("You can send your queries or requests and we will get back to you as soon as possible.");
        movie_name.setTextColor(Color.parseColor("#FFFFFFFF"));
        comment = (EditText) rootView.findViewById(R.id.comment);
        comment.setHint("Write Something...");
        add_review = (Button) rootView.findViewById(R.id.addreview);
        add_review.setEnabled(true);
        add_review.setText(" Send ");

        TextView aa= (TextView) rootView.findViewById(R.id.textView3);
        aa.setText("");



        add_review.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (user_email == null)
                    Toast.makeText(getActivity(), "Please LogIn First", Toast.LENGTH_SHORT).show();
                else
                    submitReview();
            }
        });
        return rootView;
    }

    public void submitReview(){
        Log.d(TAG, "submitReview");
        if(!validate()){
            return;
        }
        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Sending..");
        pdialog.show();
        String comment = this.comment.getText().toString();

        final String url = "http://172.16.109.69/laravel/public/appContactUs";
        Map<String ,String> params = new HashMap<String,String>();
        params.put("comment",comment);
        params.put("email", user_email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pdialog.dismiss();
                        try {
                            String ress= response.getString("msg");
                            Toast.makeText(getActivity(),ress,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);



    }

    public boolean validate(){
        boolean valid = true;

        String comm = this.comment.getText().toString();
        if(comm.isEmpty()) {
            Toast.makeText(getActivity(),"Please Write Something",Toast.LENGTH_SHORT).show();
            valid= false;
        }
        return valid;
    }


}
