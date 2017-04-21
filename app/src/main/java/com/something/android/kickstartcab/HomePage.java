package com.something.android.kickstartcab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.something.android.kickstartcab.Fragment.Config;


public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Button bookDriverButton, bookCabButton;
    private String firstName,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        final Context context = this;

        /**Checking if user not logged in then redirect to Login page **/
        if (!SessionManagement.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        /**Toolbar Functionality**/
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Home");


            /**Navigation Drawer**/
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }catch (Throwable e){
            e.printStackTrace();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        /**Initialising TextView**/

        final TextView textName = (TextView) header.findViewById(R.id.name_header);
        final TextView textEmail = (TextView) header.findViewById(R.id.email_header);
        final TextView movingText = (TextView) findViewById(R.id.mywidget);
        movingText.setSelected(true);
        movingText.setSingleLine(true);

        /**Taking name from shared preference**/
        firstName = SessionManagement.getInstance(getApplicationContext()).getName();
        email = SessionManagement.getInstance(getApplicationContext()).getEmail();

        try{
        textName.setText("Hello " + firstName + "!");}
        catch(Throwable e){
            e.printStackTrace();
        }
        try{
        textEmail.setText(email);}
        catch(Throwable e){
            e.printStackTrace();
        }

        /**Booking Button Functionality**/
        bookDriverButton = (Button) findViewById(R.id.bookDriver);
        bookCabButton = (Button) findViewById(R.id.bookCab);
        bookDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DriverBooking.class);
                startActivity(intent);
            }
        });
        bookCabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CabBooking.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_payment) {
            gotoCategory(Config.Payment_Category);
        } else if (id == R.id.nav_bookingHistory) {
            gotoCategory(Config.Booking_History_Category);

        } else if (id == R.id.nav_help) {
            gotoCategory(Config.Help_Category);

        } else if (id == R.id.nav_settings) {
            gotoCategory(Config.Setting_Category);

        } else if (id == R.id.nav_legal) {
            gotoCategory(Config.Legal_Category);
        }
        else if(id == R.id.logOut){
            SessionManagement.getInstance(getApplicationContext()).logout();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**Directing Navigation Items**/
    public void gotoCategory(String category){
        Intent intent = new Intent(getApplicationContext(),LoadAllFragment.class);
        intent.putExtra(Config.Category,category);
        startActivity(intent);
    }

    /**Overriding Back Button**/
    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        backButtonHandler();
        return;
    }

    /**Creating Alert Dialog Box**/
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                HomePage.this);
        // Setting Dialog Title
        alertDialog.setTitle("Leave application?");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to leave the application?");
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.dialog_icon);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }
}