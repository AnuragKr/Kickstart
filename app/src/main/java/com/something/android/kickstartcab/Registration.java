package com.something.android.kickstartcab;

/**
 * Created by Anurag10 on 1/12/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    //Initialization
    private String spinnerItem, spinnerAge, fName, lName, emailId, phone, pass, rePass;
    private String errorMessage = null, message = null;
    AlertDialog.Builder builder, newBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        /** Initializing Edit text**/
        final EditText edtFirst = (EditText) findViewById(R.id.first_name);
        final EditText edtLast = (EditText) findViewById(R.id.last_name);
        final EditText edtEmail = (EditText) findViewById(R.id.email_id);
        final EditText edtPhone = (EditText) findViewById(R.id.phone);
        final EditText edtPass = (EditText) findViewById(R.id.password);
        final EditText edtConfPass = (EditText) findViewById(R.id.re_password);
        final TextView helpText = (TextView) findViewById(R.id.help_vehicle);
        builder = new AlertDialog.Builder(Registration.this);
        newBuilder = new AlertDialog.Builder(Registration.this);

        /**Drop Down Options For Vehicle Selection**/
        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.vehicle_category);
        //Spinner Drop Down Element
        List<String> categories = new ArrayList<String>();
        categories.add("Choose Vehicle Type");
        categories.add("Ramp");
        categories.add("Turn-Out Wheel Chair");
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
                (tv).setGravity(Gravity.CENTER);
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


        /**Age Category Drop Down Option**/

        // Spinner element
        final Spinner ageSpinner = (Spinner) findViewById(R.id.age_category);
        //Spinner Drop Down Element
        final List<String> ageCategory = new ArrayList<String>();
        ageCategory.add("Select Age Range");
        ageCategory.add("<18");
        ageCategory.add("18-60");
        ageCategory.add(">60");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, ageCategory) {
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
                (tv).setGravity(Gravity.CENTER);
                return view;
            }
        };
        // Specify the layout to use when the list of choices appears
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ageSpinner.setAdapter(ageAdapter);
        //After Selecting Spinner Item
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerAge = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**Help Option For Vehicle Selection**/
        helpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VehicleTypeInfo.class);
                startActivity(intent);
            }
        });

        /**When Login button get clicked it will redirect to Login Screen**/
        addListenerOnButton();

        /** When Register button get clicked first it will verify all entry and then Redirected to Home Page**/
        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        /**Here Receiving all inputs entered by User**/
                        fName = edtFirst.getText().toString();
                        lName = edtLast.getText().toString();
                        emailId = edtEmail.getText().toString();
                        phone = edtPhone.getText().toString();
                        pass = edtPass.getText().toString();
                        rePass = edtConfPass.getText().toString();
                        /*Validating a first name correctly entered*/
                        if (!isValidFirstName(fName)) {
                            edtFirst.setError("Invalid First Name");
                            edtFirst.requestFocus();
                            edtPass.setText("");
                            edtConfPass.setText("");
                        }

                        /*Validating a last name correctly entered*/
                        else if (!isValidLastName(lName)) {
                            edtLast.setError("Invalid Last Name");
                            edtLast.requestFocus();
                            edtPass.setText("");
                            edtConfPass.setText("");
                        }

                        /*Validating a email correctly entered*/
                        else if (!isValidEmail(emailId)) {
                            edtEmail.setError("Invalid Email");
                            edtEmail.requestFocus();
                            edtPass.setText("");
                            edtConfPass.setText("");
                        }

                        /*Validating a phone number correctly entered*/
                        else if (!isValidPhone(phone)) {
                            edtPhone.setError("Invalid Phone Number");
                            edtPhone.requestFocus();
                            edtPass.setText("");
                            edtConfPass.setText("");
                        }
                        /*Validating a Vehicle Type*/
                        else if (spinnerItem.equals("Choose Vehicle Type")) {
                            Toast.makeText(getApplicationContext(), "Please Select Vehicle", Toast.LENGTH_LONG).show();
                            edtPass.setText("");
                            edtConfPass.setText("");
                        }
                        /*Validating a Age Category*/
                        else if (spinnerAge.equals("Select Age Range")) {
                            Toast.makeText(getApplicationContext(), "Please Select Age Range", Toast.LENGTH_LONG).show();
                            edtPass.setText("");
                            edtConfPass.setText("");
                        }

                        /*Validating a Password correctly entered*/
                        else if (!isValidPassword(pass)) {
                            builder.setTitle("Invalid Password");
                            displayAlert("Password length should be more than 6.");
                            edtPass.setText("");
                            edtConfPass.setText("");
                        }

                        /*Validating a Re-Password correctly entered*/
                        else if (!isValidRePassword(rePass)) {
                            builder.setTitle("Invalid Password");
                            displayAlert("Password length should be more than 6.");
                            edtConfPass.requestFocus();
                            edtPass.setText("");
                            edtConfPass.setText("");
                        }

                        /*Matching a Password correctly entered*/
                        else if (!pass.equals(rePass) && pass.length() != 0 && rePass.length() != 0) {
                            edtConfPass.setError("Password not matching");
                            edtConfPass.requestFocus();
                            edtPass.setText("");
                            edtConfPass.setText("");
                        } else {
                            final ProgressDialog pd = new ProgressDialog(Registration.this);
                            pd.setMessage("Loading...");
                            pd.show();
                            /**Making Database Connection**/
                            Map<String, String> params = new HashMap<>();
                            params.put("first_name", fName.substring(0, 1).toUpperCase() + fName.substring(1));
                            params.put("last_name", lName.substring(0, 1).toUpperCase() + lName.substring(1));
                            params.put("email_id", emailId);
                            params.put("age_range", spinnerAge);
                            params.put("password", pass);
                            params.put("phone_no", phone);
                            params.put("preferred_vehicle", spinnerItem);
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("https")
                                    .authority("kickstartcabs.com")
                                    .appendPath("android")
                                    .appendPath("registerJsonObject.php");
                            String registrationUrl = builder.build().toString();
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, registrationUrl,
                                    new JSONObject(params), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String code = response.getString("code");
                                        message = response.getString("message");
                                        if (code.equals("reg_failed")) {
                                            newBuilder.setTitle("Server Response");
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    pd.dismiss();
                                                    displayServerAlert(message + "Kindly try with new email-id");
                                                    edtPass.setText("");
                                                    edtConfPass.setText("");
                                                    edtEmail.setText("");
                                                }
                                            }, 5000); // 5000 milliseconds delay
                                        } else if (code.equals("reg_success")) {
                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                                            finish();
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
                                                edtPass.setText("");
                                                edtConfPass.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof ServerError) {
                                        errorMessage = "The server could not be found. Please try again after some time!!";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                edtPass.setText("");
                                                edtConfPass.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof AuthFailureError) {
                                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                edtPass.setText("");
                                                edtConfPass.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof ParseError) {
                                        errorMessage = "Parsing error! Please try again after some time!!";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                edtPass.setText("");
                                                edtConfPass.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof NoConnectionError) {
                                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                edtPass.setText("");
                                                edtConfPass.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof TimeoutError) {
                                        errorMessage = "Connection TimeOut! Please check your internet connection.";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                edtPass.setText("");
                                                edtConfPass.setText("");
                                            }
                                        }, 5000);
                                    }
                                }
                            });
                            jsonRequest.setShouldCache(false);
                            MySingleton.getInstance(Registration.this).addToRequestque(jsonRequest);
                        }
                    }
                });
    }

    /**
     * Here I am redirecting to a Login Form On clicking Login Button
     **/
    public void addListenerOnButton() {
        final Context context = this;
        Button button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {

                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(context, MainActivity.class);
                                          startActivity(intent);
                                      }
                                  }
        );
    }

    /**
     * Function For Validation
     **/
    private boolean isValidFirstName(String fName) {
        if (fName.length() == 0) {
            return false;
        } else if (!isAlpha(fName)) {
            return false;
        }
        return true;
    }

    private boolean isValidLastName(String lName) {
        if (lName.length() == 0) {
            return false;
        } else if (!isAlpha(lName)) {
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        if (email.length() == 0) {
            return false;
        } else {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();

        }
    }

    private boolean isValidPhone(String phone) {
        if (phone.length() == 0 || phone.length() < 10) {
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    private boolean isValidRePassword(String rePass) {
        if (rePass != null && rePass.length() > 6) {
            return true;
        }
        return false;
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (!Character.isWhitespace(c)) {
                if (!Character.isLetter(c)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void displayAlert(final String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void displayServerAlert(final String message) {
        newBuilder.setMessage(message);
        newBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog alertDialog = newBuilder.create();
        alertDialog.show();
    }
}