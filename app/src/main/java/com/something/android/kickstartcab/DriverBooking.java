package com.something.android.kickstartcab;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.something.android.kickstartcab.Fragment.Config;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.something.android.kickstartcab.R.layout.book_driver;

/**
 * Created by Anurag10
 */

public class DriverBooking extends AppCompatActivity implements  View.OnClickListener{
    private EditText pickupLoc;
    private String spinnerItem,spinnerCitySelection,customerId,bookingTime,bookingDate,pickLoc;
    private String errorMessage = null, message = null;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;
    private SimpleDateFormat dateFormatter, timeFormatter;
    Button btnDatePicker, btnTimePicker, bookButton;
    EditText txtDate, txtTime;
    private int mHour, mMinute,count = 0;
    List<String> dropDownOption = new ArrayList<String>();
    ExpandableRelativeLayout expandableLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(book_driver);
        customerId = SessionManagement.getInstance(getApplicationContext()).getCustomerId();
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.arrow_left);
            getSupportActionBar().setTitle(Config.Driver_Booking);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //Drop Down Option Getting From Server
        try{
            getLocationDropDownOption();}
        catch (Throwable e){
            e.printStackTrace();
        }

        //Initializing
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("H:mm");
        pickupLoc = (EditText) findViewById(R.id.pickupLocation);
        btnDatePicker = (Button) findViewById(R.id.datePicker);
        btnTimePicker = (Button) findViewById(R.id.timePicker);
        txtDate = (EditText) findViewById(R.id.dateText);
        txtTime = (EditText) findViewById(R.id.timeText);
        bookButton = (Button) findViewById(R.id.book);
        //Button for picking Date
        btnDatePicker.setOnClickListener(this);
        //Button for picking Time
        btnTimePicker.setOnClickListener(this);
        //Booking Button Click On Listener
        Button b3 = (Button) findViewById(R.id.expandableButton3);
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    expandableLayout3.toggle();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        // Spinner element For Package Selection
        final Spinner spinner = (Spinner) findViewById(R.id.packageSelection);
        // Spinner click listener
        //Spinner Drop Down Element
        List<String> categories = new ArrayList<String>();
        categories.add("Choose Package Type");
        categories.add("Inside City Package");
        categories.add("Outstation Package");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //After Selecting Spinner Item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerItem = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /**Choose City**/
        // Spinner element For Package Selection
        final Spinner spinnerCity = (Spinner) findViewById(R.id.chooseCity);
        // Spinner click listener
        //Spinner Drop Down Element
        List<String> cityCategories = new ArrayList<String>();
        cityCategories.add("Choose City");
        cityCategories.add("Bangalore");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,cityCategories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        // Specify the layout to use when the list of choices appears
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCity.setAdapter(cityAdapter);
        //After Selecting Spinner Item
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCitySelection = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**Here Receiving all inputs entered by User**/
                pickLoc = pickupLoc.getText().toString();
                bookingDate = txtDate.getText().toString();
                bookingTime = txtTime.getText().toString();
                /*Validating a Pickup Location correctly entered*/
                if (pickLoc.length() == 0) {
                    pickupLoc.setError("Invalid Location");
                    pickupLoc.requestFocus();
                } else if (bookingDate.equals("Date Of Booking")) {
                    Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_LONG).show();
                } else if (bookingTime.equals("Start Time")) {
                    Toast.makeText(getApplicationContext(), "Please Select Time", Toast.LENGTH_LONG).show();
                }else if (spinnerItem.equals("Choose Package Type")) {
                    Toast.makeText(getApplicationContext(), "Please Select Package", Toast.LENGTH_LONG).show();
                }else if (spinnerCity.equals("Choose City")) {
                    Toast.makeText(getApplicationContext(), "Please Select City", Toast.LENGTH_LONG).show();
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(DriverBooking.this);
                    pd.setMessage("Loading...");
                    pd.show();
                    Map<String, String> params = new HashMap<>();
                    params.put("pickUpLocation", pickLoc);
                    params.put("bookingDate", bookingDate);
                    params.put("startTime", bookingTime);
                    params.put("package", spinnerItem);
                    params.put("customerId", customerId);
                    params.put("serviceRequested", "DRIVER");
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("kickstartcabs.com")
                            .appendPath("android")
                            .appendPath("bookDriverJsonRequest.php");
                    String bookingUrl = builder.build().toString();
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,bookingUrl,
                            new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String code = response.getString("code");
                                message = response.getString("message");
                                if (code.equals("pending")) {
                                    pd.dismiss();
                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("bookingId",response.getString("bookingId"));
                                    editor.commit();
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                } else if (code.equals("bookingProblem")) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        }
                                    }, 5000);
                                } else if (code.equals("wrong")) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        }
                                    }, 5000);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof NetworkError) {
                                errorMessage = "Cannot connect to Internet...Please check your connection!";
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }, 5000);
                            } else if (volleyError instanceof ServerError) {
                                errorMessage = "The server could not be found. Please try again after some time!!";
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }, 5000);
                            } else if (volleyError instanceof AuthFailureError) {
                                errorMessage = "Cannot connect to Internet...Please check your connection!";
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }, 5000);
                            } else if (volleyError instanceof ParseError) {
                                errorMessage = "Parsing error! Please try again after some time!!";
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }, 5000);
                            } else if (volleyError instanceof NoConnectionError) {
                                errorMessage = "Cannot connect to Internet...Please check your connection!";
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }, 5000);
                            } else if (volleyError instanceof TimeoutError) {
                                errorMessage = "Connection TimeOut! Please check your internet connection.";
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }, 5000);
                            }
                        }
                    });
                    jsonRequest.setShouldCache(false);
                    MySingleton.getInstance(DriverBooking.this).addToRequestque(jsonRequest);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {
            Calendar newCalendar = Calendar.getInstance();
            fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    txtDate.setText(dateFormatter.format(newDate.getTime()));
                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            fromDatePickerDialog.show();
        } else if (v == btnTimePicker) {
            Calendar newTime = Calendar.getInstance();
            mHour = newTime.get(Calendar.HOUR_OF_DAY);
            mMinute = newTime.get(Calendar.MINUTE);
            fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                    bookingTime = String.valueOf(selectedHour)+ ":" + String.valueOf(selectedMinute);
                    if (selectedHour > 12) {
                        if((String.valueOf(selectedMinute).length() == 1)){
                            txtTime.setText(String.valueOf(selectedHour - 12) + ":" + "0" + (String.valueOf(selectedMinute) + " PM"));
                        }
                        else{
                            txtTime.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf(selectedMinute) + " PM"));}
                    } else if (selectedHour == 12) {
                        if((String.valueOf(selectedMinute).length() == 1)){
                            txtTime.setText("12" + ":" + "0" + (String.valueOf(selectedHour) + " PM"));
                        }
                        else{
                            txtTime.setText("12" + ":" + (String.valueOf(selectedHour) + " PM"));}
                    } else if (selectedHour < 12) {
                        if (selectedHour != 0) {
                            if((String.valueOf(selectedMinute).length() == 1)){
                                txtTime.setText(String.valueOf(selectedHour) + ":" + "0" + (String.valueOf(selectedMinute) + " AM"));
                            }
                            else{
                                txtTime.setText(String.valueOf(selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));}
                        } else {
                            if((String.valueOf(selectedMinute).length() == 1)){
                                txtTime.setText("12" + ":" + "0" + (String.valueOf(selectedMinute) + " AM"));
                            }
                            else{
                                txtTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " AM"));}
                        }
                    }
                }
            }, mHour, mMinute, false);
            fromTimePickerDialog.show();
        }
    }

    //Drop Down Option For Location
    private void getLocationDropDownOption() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("kickstartcabs.com")
                .appendPath("android")
                .appendPath("sendAddressChoice.php")
                .appendQueryParameter("customer_id",customerId);
        String sendAddressChoiceUrl = builder.build().toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(sendAddressChoiceUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Calling method parseData to parse the json response
                JSONObject json = null;
                try {
                    //Getting json
                    json = response.getJSONObject(0);
                    count = Integer.parseInt(json.getString(Config.Total_Number_Of_Address_Saved));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(count > 0){
                    parseData(response);
                }
                else
                    return;
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NetworkError) {
                    errorMessage = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    errorMessage = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    errorMessage = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    errorMessage = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    errorMessage = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    errorMessage = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
        jsonArrayRequest.setShouldCache(false);
        MySingleton.getInstance(DriverBooking.this).addToRequestque(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                dropDownOption.add(json.getString(Config.Saved_Address));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String[] dropDown = new String[dropDownOption.size()];
        dropDownOption.toArray( dropDown );
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.pickupLocation);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_item,R.id.autoCompleteItem,dropDown);
        textView.setAdapter(adapter);
        textView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                textView.showDropDown();
                return false;
            }
        });
    }

}
