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
/**Billing Card Adapter**/

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context context;

    //List to store all superheroes
    private List<BillingInfo> billingInfos;

    //Constructor of this class
    public CardAdapter(List<BillingInfo> billingInfos, Context context){
        super();
        //Getting all  bookingInfos
        this.billingInfos = billingInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.billing_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        BillingInfo billingInfo =  billingInfos.get(position);

        //Showing data on the views
        holder.billNumber.setText(billingInfo.getBillNumber());
        holder.totalDriveTime.setText(billingInfo.getTotalDriveTime());
        holder.totalAmount.setText(billingInfo.getTotalAmount());
        holder.paymentStatus.setText(billingInfo.getPaymentStatus());

    }

    @Override
    public int getItemCount() {
        return billingInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView billNumber;
        public TextView totalDriveTime;
        public TextView paymentStatus;
        public TextView totalAmount;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            billNumber = (TextView) itemView.findViewById(R.id.billNumber);
            totalDriveTime = (TextView) itemView.findViewById(R.id.totalDriveTime);
            totalAmount = (TextView) itemView.findViewById(R.id.totalAmount);
            paymentStatus = (TextView) itemView.findViewById(R.id.paymentStatus);
        }
    }
}
