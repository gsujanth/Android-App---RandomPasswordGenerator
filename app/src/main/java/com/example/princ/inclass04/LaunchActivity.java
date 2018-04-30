package com.example.princ.inclass04;
/*
  Author : Sujanth Babu Guntupalli
*/
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        setTitle("Welcome");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }
}
