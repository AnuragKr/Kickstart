package com.something.android.kickstartcab.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.something.android.kickstartcab.HomePage;
import com.something.android.kickstartcab.MySingleton;
import com.something.android.kickstartcab.R;
import com.something.android.kickstartcab.SessionManagement;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    ExpandableRelativeLayout expandableLayout1;
    private String customerId;
    private String errorMessage = null, message = null;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        try {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.arrow_left);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Config.Setting_Category);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Button b1 = (Button) view.findViewById(R.id.expandableButton1);
        expandableLayout1 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout1);
        b1.setOnClickListener(this);


        /** Initialization **/
        final EditText edtHouse = (EditText) view.findViewById(R.id.house_number);
        final EditText edtArea = (EditText) view.findViewById(R.id.area);
        final EditText edtLandmark = (EditText) view.findViewById(R.id.landmark);
        /**Adding Address**/
        Button addAddressButton = (Button) view.findViewById(R.id.addAddress);
        addAddressButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        /**Here Receiving all inputs entered by User**/
                        final String houseNumber = edtHouse.getText().toString();
                        final String areaName = edtArea.getText().toString();
                        final String landmarkName = edtLandmark.getText().toString();

                        if (!isValidHouseNumber(houseNumber)) {
                            edtHouse.setError("House Number not entered");
                            edtHouse.requestFocus();
                            edtHouse.setText("");
                        }

                        /*Validating a last name correctly entered*/
                        else if (!isValidAreaName(areaName)) {
                            edtArea.setError("Area name not entered");
                            edtArea.requestFocus();
                            edtArea.setText("");
                        }

                        /*Validating a email correctly entered*/
                        else if (!isValidLandMarkName(landmarkName)) {
                            edtLandmark.setError("Landmark Name not entered");
                            edtLandmark.requestFocus();
                            edtLandmark.setText("");
                        }
                        else {
                            addAddressOnServer(houseNumber,areaName,landmarkName);
                        }
                    }
                });
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.expandableButton1:
                try {
                    expandableLayout1.toggle();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private boolean isValidHouseNumber(String houseNumber) {
        if (houseNumber.length() == 0) {
            return false;
        }
        return true;
    }

    private boolean isValidAreaName(String areaName) {
        if (areaName.length() == 0) {
            return false;
        }
        return true;
    }

    private boolean isValidLandMarkName(String landmarkName) {
        if (landmarkName.length() == 0) {
            return false;
        }
        return true;
    }

    private void addAddressOnServer(String houseNumber,String areaName,String landmarkName) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();
        Map<String, String> params = new HashMap<>();
        customerId = SessionManagement.getInstance(getContext()).getCustomerId();
        params.put("customer_id", customerId);
        params.put("houseNumber", houseNumber);
        params.put("areaName", areaName);
        params.put("landmarkName", landmarkName);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("kickstartcabs.com")
                .appendPath("android")
                .appendPath("save_address.php");
        String saveAddressUrl = builder.build().toString();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, saveAddressUrl,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String code = response.getString("code");
                    message = response.getString("message");
                    if (code.equals("reg_failure")) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }, 5000);
                    } else {
                        pd.dismiss();
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(),HomePage.class);
                        startActivity(intent);
                        getActivity().finish();
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
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }, 5000);
                } else if (volleyError instanceof ServerError) {
                    errorMessage = "The server could not be found. Please try again after some time!!";
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }, 5000);
                } else if (volleyError instanceof AuthFailureError) {
                    errorMessage = "Cannot connect to Internet...Please check your connection!";
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }, 5000);
                } else if (volleyError instanceof ParseError) {
                    errorMessage = "Parsing error! Please try again after some time!!";
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }, 5000);
                } else if (volleyError instanceof NoConnectionError) {
                    errorMessage = "Cannot connect to Internet...Please check your connection!";
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }, 5000);
                } else if (volleyError instanceof TimeoutError) {
                    errorMessage = "Connection TimeOut! Please check your internet connection.";
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }, 5000);
                }
            }
        });
        jsonRequest.setShouldCache(false);
        MySingleton.getInstance(getContext()).addToRequestque(jsonRequest);
    }

}