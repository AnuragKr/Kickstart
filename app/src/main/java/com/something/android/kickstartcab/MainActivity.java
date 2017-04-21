package com.something.android.kickstartcab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    // Session Manager Class
    SessionManagement session;
    private String errorMessage = null, message = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Re-Directing
        addListenerOnButton();

        /**Initializing Button And Edit Text**/
        Button loginButton = (Button) findViewById(R.id.profile);
        final EditText userEmail = (EditText) findViewById(R.id.userEmail);
        final EditText passWord = (EditText) findViewById(R.id.password);
        final TextView forgotPassword = (TextView) findViewById(R.id.forgot_password);
        final Context context = this;

        /**Here I am checking session Whether user Logged In or Not**/
        if (SessionManagement.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        }
        /**Forgot Password**/
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForgotPassword.class);
                startActivity(intent);
            }
        });

        /**Login Button Functionality**/
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        final String emailId = userEmail.getText().toString();
                        final String pass = passWord.getText().toString();
                        if (!isValidEmail(emailId)) {
                            userEmail.requestFocus();
                            userEmail.setError("Invalid Email");
                            passWord.setText("");
                        } else if (!isValidPassword(pass)) {
                            passWord.requestFocus();
                            passWord.setError("Invalid Password");
                            passWord.setText("");
                        } else {
                            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                            pd.setMessage("Loading...");
                            pd.show();
                            Map<String, String> params = new HashMap<>();
                            params.put("email_id", emailId);
                            params.put("password", pass);
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("https")
                                    .authority("kickstartcabs.com")
                                    .appendPath("android")
                                    .appendPath("loginJsonRequest.php");
                            String loginUrl = builder.build().toString();
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, loginUrl,
                                    new JSONObject(params), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //pd.cancel();
                                    try {
                                        String code = response.getString("code");
                                        message = response.getString("message");
                                        if (code.equals("login_failed")) {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    pd.dismiss();
                                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                                    passWord.setText("");
                                                    userEmail.setText("");
                                                }
                                            }, 5000);
                                        } else {
                                            pd.dismiss();
                                            SessionManagement.getInstance(getApplicationContext())
                                                    .userLogin(response.getString("name"), response.getString("email"), response.getString("vehicleType"), response.getString("customerId"));
                                            Intent intent = new Intent(context, HomePage.class);
                                            startActivity(intent);
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
                                                passWord.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof ServerError) {
                                        errorMessage = "The server could not be found. Please try again after some time!!";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                passWord.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof AuthFailureError) {
                                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                passWord.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof ParseError) {
                                        errorMessage = "Parsing error! Please try again after some time!!";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                passWord.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof NoConnectionError) {
                                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                passWord.setText("");
                                            }
                                        }, 5000);
                                    } else if (volleyError instanceof TimeoutError) {
                                        errorMessage = "Connection TimeOut! Please check your internet connection.";
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                passWord.setText("");
                                            }
                                        }, 5000);
                                    }
                                }
                            });
                            jsonRequest.setShouldCache(false);
                            MySingleton.getInstance(MainActivity.this).addToRequestque(jsonRequest);
                        }
                    }
                });

    }

    /**
     * Here I am redirecting to a Registeration Form On clicking Register Button
     **/
    public void addListenerOnButton() {
        final Context context = this;
        Button button = (Button) findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener() {

                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(context, Registration.class);
                                          startActivity(intent);
                                      }
                                  }
        );
    }

    /**
     * Validation Functionality
     **/

    //validating email id
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

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    /**
     * Overriding OnBackPressed
     **/
    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        finish();
    }
}