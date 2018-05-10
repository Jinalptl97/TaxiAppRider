package com.taxibookingrider.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by admin on 12/5/2017.
 */
@IgnoreExtraProperties
public class DriverInfo {

    String driverid, fname, lname, car, carModel, device_token, color, selected, driverProfilepic, car_number;
    Float distance;
    public Float dfare;
    Float rating;

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public DriverInfo(Float dfare) {
        this.dfare = dfare;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getDriverProfilepic() {
        return driverProfilepic;
    }

    public void setDriverProfilepic(String driverProfilepic) {
        this.driverProfilepic = driverProfilepic;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }
}
