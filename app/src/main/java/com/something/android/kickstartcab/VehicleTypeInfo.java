package com.something.android.kickstartcab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Anurag10 on 3/16/2017.
 */

public class VehicleTypeInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicletypeinfo);
    }
    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        finish();
    }
}
