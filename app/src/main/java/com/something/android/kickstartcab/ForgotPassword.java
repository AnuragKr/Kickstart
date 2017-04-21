package com.something.android.kickstartcab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.something.android.kickstartcab.Fragment.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anurag10 on 4/1/2017.
 */

public class ForgotPassword extends AppCompatActivity {
    private String emailId,pass,rePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        /**Initializing Button And Edit Text**/
        final EditText userEmail = (EditText) findViewById(R.id.email_id);
        final Button forgotPasswordButton = (Button) findViewById(R.id.forgot_password);
        final EditText edtPass = (EditText) findViewById(R.id.password);
        final EditText edtConfPass = (EditText) findViewById(R.id.re_password);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.arrow_left);
            getSupportActionBar().setTitle(Config.FORGOT_PASSWORD);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch(Throwable e){
            e.printStackTrace();
        }

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailId = userEmail.getText().toString();
                pass = edtPass.getText().toString();
                rePass = edtConfPass.getText().toString();
                if (!isValidEmail(emailId)) {
                    userEmail.requestFocus();
                    userEmail.setError("Invalid Email");}
                  /*Validating a Password correctly entered*/
                else if (!isValidPassword(pass)) {
                    edtPass.setError("Password not entered");
                    edtPass.requestFocus();
                    edtPass.setText("");
                    edtConfPass.setText("");
                }

                        /*Validating a Re-Password correctly entered*/
                else if (!isValidRePassword(rePass)) {
                    edtConfPass.setError("Please confirm password");
                    edtConfPass.requestFocus();
                    edtPass.setText("");
                    edtConfPass.setText("");
                }

                        /*Matching a Password correctly entered*/
                else if (!pass.equals(rePass) && pass.length() != 0 && rePass.length() != 0) {
                    edtConfPass.setError("Password Not matched");
                    edtConfPass.requestFocus();
                    edtPass.setText("");
                    edtConfPass.setText("");}
                else{
                checkEmailAndUpdatePassword(emailId);}
            }
        });
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

    /**
     * Overriding OnBackPressed
     **/
    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        finish();
    }
/**Check Email**/
    private void checkEmailAndUpdatePassword(String emailId) {
        final ProgressDialog pd = new ProgressDialog(ForgotPassword.this);
        pd.setMessage("Checking...");
        pd.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pd.dismiss();
            }
        }, 5000); // 5000 milliseconds delay
        Map<String, String> params = new HashMap<>();
        params.put("email_id", emailId);
        params.put("password",pass);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("kickstartcabs.com")
                .appendPath("android")
                .appendPath("forgotPassword.php");
        String forgotPasswordUrl = builder.build().toString();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, forgotPasswordUrl,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.cancel();
                try {
                    String code = response.getString("code");
                    String message = response.getString("message");
                    if (code.equals("failed")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        });
        jsonRequest.setShouldCache(false);
        MySingleton.getInstance(ForgotPassword.this).addToRequestque(jsonRequest);
    }
}