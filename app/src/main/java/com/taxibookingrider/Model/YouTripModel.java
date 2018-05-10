package com.taxibookingrider.Model;

/**
 * Created by admin on 07-Feb-18.
 */

public class YouTripModel {

    String tripid, payment, cartype, from, to, day, date, flag, fare, finalfare, time, paymentamt, wamt, flat, flong, tlat, tlong;
    public String type;

    public YouTripModel(String type) {
        this.type = type;
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getFinalfare() {
        return finalfare;
    }

    public void setFinalfare(String finalfare) {
        this.finalfare = finalfare;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPaymentamt() {
        return paymentamt;
    }

    public void setPaymentamt(String paymentamt) {
        this.paymentamt = paymentamt;
    }

    public String getWamt() {
        return wamt;
    }

    public void setWamt(String wamt) {
        this.wamt = wamt;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getFlong() {
        return flong;
    }

    public void setFlong(String flong) {
        this.flong = flong;
    }

    public String getTlat() {
        return tlat;
    }

    public void setTlat(String tlat) {
        this.tlat = tlat;
    }

    public String getTlong() {
        return tlong;
    }

    public void setTlong(String tlong) {
        this.tlong = tlong;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
