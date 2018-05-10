package com.taxibookingrider.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Pref_Master {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private String str_user_id = "uid";
    private String user_id = "0";

    private String str_user_number = "unumber";
    private String user_number = "0";

    private String str_user_firstname = "firstname";
    private String firstname = "";


    private String str_user_lastname = "lastname";
    private String lastname = "";

    private String str_user_password = "password";
    private String password = "";


    private String str_user_email = "email";
    private String email = "";

    private String str_userimage = "userimage";
    private String userimage = "";

    private String str_driverid = "driverid";
    private String driverid = "";

    private String str_fromlocationtrip = "fromlocationtrip";
    private String fromlocationtrip = "";

    private String str_tolocationtrip = "tolocationtrip";
    private String tolocationtrip = "";

    private String str_tripeta = "tripeta";
    private String tripeta = "";

    private String str_fromaddress = "fromaddress";
    private String fromaddress = "";

    private String str_toaddress = "toaddress";
    private String toaddress = "";

    private String str_distance = "distance";
    private String distance = "";

    private String str_driverfare = "driverfare";
    private String driverfare = "";

    private String str_driverregistrationnumber = "driverregistrationnumber";
    private String driverregistrationnumber = "";

    private String str_ctype = "ctype";
    private String ctype = "";


    private String str_drivertokem = "drivertoken";
    private String drivertoken = "";

    private String str_tripid = "tripid";
    private String tripid = "";

    private String str_driverprofilepic = "driverprofilepic";
    private String driverprofilepic = "";

    private String str_driverfname = "driverfname";
    private String driverfname = "";

    private String str_endon = "endon";
    private String endon = "";

    private String str_stdon = "stdon";
    private String stdon = "";

    private String str_driverlname = "driverlname";
    private String driverlname = "";

    private String str_autostart = "autostart";
    private int auto = 0;

    private String str_defaultcancel = "defaultcancel";
    private String defaultcancel = "";


    private String str_awsurl = "awsurl";
    private String awsurl = "";

    private String str_userfolder = "userfolder";
    private String userfolder = "";

    private String str_tolat = "tolat";
    private String tolat = "";

    private String str_tolng= "tolng";
    private String tolng = "";



    private String str_tripId= "tripId";
    private String tripId = "";



    private String str_driverId= "driverId";
    private String driverId = "";

    private String str_language = "language";
    private String language = "";

/*

    private String str_user_toaddress = "toaddress";
    private String toaddress = "";
*/


    // 0 = English , 1 = Arabic , 3 = first
    Context context;

    public Pref_Master(Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getUser_number() {
        return pref.getString(str_user_number, user_number);
    }

    public void setUser_number(String name) {
        editor = pref.edit();
        editor.putString(str_user_number, name);
        editor.apply();
    }

    public String getUID() {
        return pref.getString(str_user_id, user_id);
    }

    public void setUID(String name) {
        editor = pref.edit();
        editor.putString(str_user_id, name);
        editor.apply();
    }

    public String getLanguage() {
        return pref.getString(str_language, language);
    }


    public void setLanguage(String name) {
        editor = pref.edit();
        editor.putString(str_language, name);
        editor.apply();
    }


    public String getFirstname() {
        return pref.getString(str_user_firstname, firstname);
    }

    public void setFirstname(String name) {
        editor = pref.edit();
        editor.putString(str_user_firstname, name);
        editor.apply();
    }


    public String getLastname() {
        return pref.getString(str_user_lastname, lastname);
    }

    public void setLastname(String name) {
        editor = pref.edit();
        editor.putString(str_user_lastname, name);
        editor.apply();
    }


    public String getPassword() {
        return pref.getString(str_user_password, password);
    }

    public void setPassword(String name) {
        editor = pref.edit();
        editor.putString(str_user_password, name);
        editor.apply();
    }


    public String getEmail() {
        return pref.getString(str_user_email, email);
    }

    public void setEmail(String name) {
        editor = pref.edit();
        editor.putString(str_user_email, name);
        editor.apply();
    }

    public String getUserimage() {
        return pref.getString(str_userimage, userimage);
    }

    public void setUserimage(String name) {
        editor = pref.edit();
        editor.putString(str_userimage, name);
        editor.apply();
    }

    public String getDriverid() {
        return pref.getString(str_driverid, driverid);
    }

    public void setDriverid(String name) {
        editor = pref.edit();
        editor.putString(str_driverid, name);
        editor.apply();
    }

    public String getFromlocationtrip() {
        return pref.getString(str_fromlocationtrip, fromlocationtrip);
    }

    public void setFromlocationtrip(String name) {
        editor = pref.edit();
        editor.putString(str_fromlocationtrip, name);
        editor.apply();
    }

    public String getTolocationtrip() {
        return pref.getString(str_tolocationtrip, tolocationtrip);
    }

    public void setTolocationtrip(String name) {
        editor = pref.edit();
        editor.putString(str_tolocationtrip, name);
        editor.apply();
    }

    public String getFromaddress() {
        return pref.getString(str_fromaddress, fromaddress);
    }

    public void setFromaddress(String name) {
        editor = pref.edit();
        editor.putString(str_fromaddress, name);
        editor.apply();
    }


    public String getToaddress() {
        return pref.getString(str_toaddress, toaddress);
    }

    public void setToaddress(String name) {
        editor = pref.edit();
        editor.putString(str_toaddress, name);
        editor.apply();
    }


    public String getDistance() {
        return pref.getString(str_distance, distance);
    }

    public void setDistance(String name) {
        editor = pref.edit();
        editor.putString(str_distance, name);
        editor.apply();
    }

    public String getStr_tripId() {
        return pref.getString(str_tripId, tripId);
    }

    public void setStr_tripId(String str_tripId) {
        editor = pref.edit();
        editor.putString(str_tripId, tripId);
        editor.apply();
    }

    public String getStr_driverId() {
        return pref.getString(str_driverId, driverId);
    }

    public void setStr_driverId(String str_driverId) {
        editor = pref.edit();
        editor.putString(str_driverId, driverId);
        editor.apply();
    }

    public String getDriverfare() {
        return pref.getString(str_driverfare, driverfare);
    }

    public void setDriverfare(String name) {
        editor = pref.edit();
        editor.putString(str_driverfare, name);
        editor.apply();
    }

    public String getDriverregistrationnumber() {
        return pref.getString(str_driverregistrationnumber, driverregistrationnumber);
    }

    public void setDriverregistrationnumber(String name) {
        editor = pref.edit();
        editor.putString(str_driverregistrationnumber, name);
        editor.apply();
    }

    public String getCtype() {
        return pref.getString(str_ctype, ctype);
    }

    public void setCtype(String name) {
        editor = pref.edit();
        editor.putString(str_ctype, name);
        editor.apply();
    }

    public String getTripeta() {
        return pref.getString(str_tripeta, tripeta);
    }

    public void setTripeta(String name) {
        editor = pref.edit();
        editor.putString(str_tripeta, name);
        editor.apply();
    }

    public String getDrivertoken() {
        return pref.getString(str_drivertokem, drivertoken);
    }

    public void setDrivertoken(String name) {
        editor = pref.edit();
        editor.putString(str_drivertokem, name);
        editor.apply();
    }




    public String getTripid() {
        return pref.getString(str_tripid, tripid);
    }

    public void setTripid(String name) {
        editor = pref.edit();
        editor.putString(str_tripid, name);
        editor.apply();
    }

    public String getDriverprofilepic() {
        return pref.getString(str_driverprofilepic, driverprofilepic);
    }

    public void setDriverprofilepic(String name) {
        editor = pref.edit();
        editor.putString(str_driverprofilepic, name);
        editor.apply();
    }

    public String getDriverfname() {
        return pref.getString(str_driverfname, driverfname);
    }

    public void setDriverfname(String name) {
        editor = pref.edit();
        editor.putString(str_driverfname, name);
        editor.apply();
    }

    public String getDriverlname() {
        return pref.getString(str_driverlname, driverlname);
    }

    public void setDriverlname(String name) {
        editor = pref.edit();
        editor.putString(str_driverlname, name);
        editor.apply();
    }

    public String getStdon() {
        return pref.getString(str_stdon, stdon);
    }

    public void setStdon(String name) {
        editor = pref.edit();
        editor.putString(str_stdon, name);
        editor.apply();
    }

    public String getEndon() {
        return pref.getString(str_endon, endon);
    }

    public void setEndon(String name) {
        editor = pref.edit();
        editor.putString(str_endon, name);
        editor.apply();
    }


    public int getAutostart() {
        return pref.getInt(str_autostart, auto);
    }

    public void setAuto(int name) {
        editor = pref.edit();
        editor.putInt(str_autostart, name);
        editor.apply();
    }

    public String getDefaultcancel() {
        return pref.getString(str_defaultcancel, defaultcancel);
    }

    public void setDefaultcancel(String name) {
        editor = pref.edit();
        editor.putString(str_defaultcancel, name);
        editor.apply();
    }

    public String getAwsurl() {
        return pref.getString(str_awsurl, awsurl);
    }

    public void setAwsurl(String name) {
        editor = pref.edit();
        editor.putString(str_awsurl, name);
        editor.apply();
    }



    public String getTolng() {
        return pref.getString(str_tolat, tolat);
    }

    public void setTolng(String name) {
        editor = pref.edit();
        editor.putString(str_tolat, name);
        editor.apply();
    }


    public String getTolat() {
        return pref.getString(str_tolng, tolng);
    }

    public void setTolat(String name) {
        editor = pref.edit();
        editor.putString(str_tolng, name);
        editor.apply();
    }


    public String getUserfolder() {
        return pref.getString(str_userfolder, userfolder);
    }

    public void setUserfolder(String name) {
        editor = pref.edit();
        editor.putString(str_userfolder, name);
        editor.apply();
    }



    public void clear_pref() {
        pref.edit().clear().apply();
    }
}
