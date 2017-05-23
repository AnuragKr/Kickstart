package com.something.android.kickstartcab.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.something.android.kickstartcab.R;
import com.something.android.kickstartcab.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewBookingHistory extends Fragment {
    //Creating a List of Booking History
    private List<BookingInfo> listBookingInfos;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private String customerId;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1, temp = 1, maxLimit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        customerId = SessionManagement.getInstance(getContext()).getCustomerId();
        return inflater.inflate(R.layout.fragment_booking_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.arrow_left);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Config.Booking_History_Category);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        listBookingInfos = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());

        //Calling method to get data to fetch data
        getData();
        final RecyclerView.OnScrollListener mOnScrollChangeListener = new RecyclerView.OnScrollListener() {
            //Overriden method to detect scrolling
            @Override
            public void onScrolled(RecyclerView recyclerView, int scrollX, int scrollY) {
                //Ifscrolled at last then
                if (isLastItemDisplaying(recyclerView)) {
                    if (temp <= maxLimit) {
                        getData();
                    } else {
                        Toast.makeText(getContext(), "No More Booking History", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        };

        //Adding an scroll change listener to recyclerview
        recyclerView.addOnScrollListener(mOnScrollChangeListener);

        //initializing our adapter
        adapter = new BookingAdapter(listBookingInfos, getContext());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Displaying Progressbar
        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("kickstartcabs.com")
                .appendPath("android")
                .appendPath("bookingCustomerHistory.php")
                .appendQueryParameter("page", String.valueOf(requestCount))
                .appendQueryParameter("customerId", customerId);
        String bookingHistoryUrl = builder.build().toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(bookingHistoryUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Calling method parseData to parse the json response
                Log.v("Response", response.toString());
                parseData(response);
                //Hiding the progressbar
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        });
        //Returning the request
        jsonArrayRequest.setShouldCache(false);
        return jsonArrayRequest;
    }

    //This method will get data from the web api
    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the BookingInfo object
            BookingInfo bookingInfo = new BookingInfo();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                bookingInfo.setBookingId(json.getString(Config.TAG_BOOKING_ID));
                bookingInfo.setStartDate(json.getString(Config.TAG_START_DATE));
                bookingInfo.setStartTime(json.getString(Config.TAG_START_TIME));
                bookingInfo.setVehicleType(json.getString(Config.TAG_VEHICLE_TYPE));
                try {
                    String[] packageType = json.getString(Config.TAG_PACKAGE_TYPE).split("\\+");
                    String packageInfo = packageType[1];
                    bookingInfo.setPackageType(packageInfo);
                    Log.v("Package", packageInfo);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                bookingInfo.setBookingStatus(json.getString(Config.TAG_BOOKING_STATUS));
                maxLimit = Integer.parseInt(json.getString(Config.TAG_MAX_LIMIT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            listBookingInfos.add(bookingInfo);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0 && recyclerView.getAdapter().getItemCount() <= 7) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            Log.d("Initial Temp", "" + temp);
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                temp++;
                return true;
            }
        }
        return false;
    }
}