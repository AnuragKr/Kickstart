package com.something.android.kickstartcab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.something.android.kickstartcab.Fragment.BillingHistory;
import com.something.android.kickstartcab.Fragment.Config;
import com.something.android.kickstartcab.Fragment.HelpFragment;
import com.something.android.kickstartcab.Fragment.LegalFragment;
import com.something.android.kickstartcab.Fragment.NewBookingHistory;
import com.something.android.kickstartcab.Fragment.PaymentFragment;
import com.something.android.kickstartcab.Fragment.SettingFragment;

/**
 * Created by Anurag10 on 3/2/2017.
 */

public class LoadAllFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_all_fragment);
        Intent intent = getIntent();
        String getCategory = intent.getStringExtra(Config.Category);
        FragmentManager manager = getSupportFragmentManager();
        switch(getCategory){
            case Config.Payment_Category:
                PaymentFragment paymentFragment = new PaymentFragment();
                manager.beginTransaction().replace(R.id.
                        load_fragment,paymentFragment).addToBackStack(null).commit();
                break;
            case Config.Booking_History_Category:
                NewBookingHistory bookingHistoryFragment = new NewBookingHistory();
                manager.beginTransaction().replace(R.id.load_fragment,bookingHistoryFragment).addToBackStack(null).commit();
                break;
            case Config.Billing_History_Category:
                BillingHistory billingHistoryFragment = new BillingHistory();
                manager.beginTransaction().replace(R.id.load_fragment,billingHistoryFragment).addToBackStack(null).commit();
                break;
            case Config.Setting_Category:
                SettingFragment settingFragment = new SettingFragment();
                manager.beginTransaction().replace(R.id.load_fragment,settingFragment).addToBackStack(null).commit();
                break;
            case Config.Help_Category:
                HelpFragment helpFragment = new HelpFragment();
                manager.beginTransaction().replace(R.id.load_fragment,helpFragment).addToBackStack(null).commit();
                break;
            case Config.Legal_Category:
                LegalFragment legalFragment = new LegalFragment();
                manager.beginTransaction().replace(R.id.load_fragment,legalFragment).addToBackStack(null).commit();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
