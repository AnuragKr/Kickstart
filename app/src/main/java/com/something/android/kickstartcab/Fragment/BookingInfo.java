package com.something.android.kickstartcab.Fragment;

/**
 * Created by Anurag10 on 2/28/2017.
 */

public class BookingInfo {
    //Data Variables
    private String billNumber;
    private String totalDriveTime;
    private String totalAmount;
    private String paymentStatus;

    public String getBillNumber(){
        return billNumber;
    }

    public void setBillNumber(String billNumber){
        this.billNumber = billNumber;
    }
    public String getTotalDriveTime(){
        return totalDriveTime;
    }

    public void setTotalDriveTime(String totalDriveTime){
        this.totalDriveTime = totalDriveTime;
    }

    public String getTotalAmount(){
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount){
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus(){
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus){
        this.paymentStatus = paymentStatus;
    }
}
