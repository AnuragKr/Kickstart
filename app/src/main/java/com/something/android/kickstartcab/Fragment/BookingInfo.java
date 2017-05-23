package com.something.android.kickstartcab.Fragment;

/**
 * Created by Anurag10 on 2/28/2017.
 */

public class BookingInfo {
    //Data Variables
    private String bookingId;
    private String startDate;
    private String startTime;
    private String vehicleType;
    private String packageType;
    private String bookingStatus;

    public String getBookingId(){
        return bookingId;
    }

    public void setBookingId(String bookingId){
        this.bookingId = bookingId;
    }

    public String getStartDate(){
        return startDate;
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getStartTime(){
        return startTime;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public String getVehicleType(){
        return vehicleType;
    }

    public void setVehicleType(String vehicleType){
        this.vehicleType = vehicleType;
    }

    public String getPackageType(){
        return packageType;
    }

    public void setPackageType(String packageType){
        this.packageType = packageType;
    }

    public String getBookingStatus(){
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus){
        this.bookingStatus = bookingStatus;
    }

}
