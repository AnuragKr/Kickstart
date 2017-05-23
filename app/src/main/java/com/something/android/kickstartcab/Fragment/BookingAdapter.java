package com.something.android.kickstartcab.Fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.something.android.kickstartcab.R;

import java.util.List;

/**
 * Created by Anurag10 on 2/28/2017.
 */
/**Booking Card Adapter**/

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context context;

    //List to store all superheroes
    private List<BookingInfo> bookingInfos;

    //Constructor of this class
    public BookingAdapter(List<BookingInfo> bookingInfos, Context context){
        super();
        //Getting all  bookingInfos
        this.bookingInfos = bookingInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        BookingInfo bookingInfo =  bookingInfos.get(position);

        //Showing data on the views
        holder.bookingId.setText(bookingInfo.getBookingId());
        holder.startDate.setText(bookingInfo.getStartDate());
        holder.startTime.setText(bookingInfo.getStartTime());
        holder.vehicleType.setText(bookingInfo.getVehicleType());
        holder.packageType.setText(bookingInfo.getPackageType());
        holder.bookingStatus.setText(bookingInfo.getBookingStatus());

    }

    @Override
    public int getItemCount() {
        return bookingInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView bookingId;
        public TextView startDate;
        public TextView startTime;
        public TextView vehicleType;
        public TextView packageType;
        public TextView bookingStatus;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            bookingId = (TextView) itemView.findViewById(R.id.bookingId);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            startTime = (TextView) itemView.findViewById(R.id.startTime);
            vehicleType = (TextView) itemView.findViewById(R.id.vehicle_type_info);
            packageType = (TextView) itemView.findViewById(R.id.packageType);
            bookingStatus = (TextView) itemView.findViewById(R.id.bookingStatus);
        }
    }
}
