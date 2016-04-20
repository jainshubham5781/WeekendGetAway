package com.example.shubham.myapplication3;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


//actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
public class MainActivity extends ActionBarActivity {
private Boolean exit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        getSupportActionBar().hide();
    }



    // link to movies
    public void gotomovies(View v){
        Intent intent= new Intent(this, MainNavigation.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);// finish activity
        } else {
            Toast.makeText(MainActivity.this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

}
