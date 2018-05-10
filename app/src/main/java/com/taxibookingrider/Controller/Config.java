package com.taxibookingrider.Controller;


import android.content.Context;
import android.content.res.Configuration;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Config {

    public static String baseurl = "https://enj4scog4b.execute-api.us-east-1.amazonaws.com/beta/";


    public static String checkmobile = baseurl + "chekmobile";
    public static String register = baseurl + "register-rider";
    public static String login = baseurl + "login";
    public static String resend = baseurl + "resendotp/";
    public static String resetpassword = baseurl + "resetpassword";
    public static String getProfile = baseurl + "getprofile/";
    public static String getSetting = baseurl + "getsettings";
    public static String updateprofile = baseurl + "updateprofile";
    public static String getfavlocation = baseurl + "viewfavlocationlist";
    public static String addtrip = baseurl + "addtrip";
    public static String tripinfo = baseurl + "tripinforider";
    public static String canceltrip = baseurl + "canceltrip";
    public static String ridertriplist = baseurl + "ridertriplist";
    public static String addmsg = baseurl + "addchatmsg";
    public static String removefavlocation = baseurl + "removefavlocation";
    public static String trippaymentdetails = baseurl + "trippaymentdetails";
    public static String receivepayment = baseurl + "receivepayment";
    public static String addrating = baseurl + "addrating";
    public static String logout = baseurl + "logout";
    public static String deleteaccount = baseurl + "deleteaccount";
    public static String checkversion = baseurl + "checkversion";
    public static String headkey = "application/json";
    public static String apikey ="wgNv4qZdy52i1M3bIjEcwap9lKl64MgQ8IcLZq0c";
    public static String currency = "$";


    public static Map<String, String> getHeaderParam(Context context) {

        Pref_Master pref_master = new Pref_Master(context);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", Config.headkey);
        header.put("x-api-key", Config.apikey);
        return header;
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        //emailRegEx = "" + R.string.email_string;
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    public static void Select_Status(ArrayList<LinearLayout> ll, int pos, ArrayList<ImageView> Arr_Chk_cat) {
        for (int i = 0; i < ll.size(); i++) {
            Arr_Chk_cat.get(i).setImageResource((i == pos) ? R.drawable.selected1 : R.drawable.not_selected);
        }

    }

    public void Change_Language(Context context, String lang) {
//0 = English 1 = Arabic
        Locale locale = new Locale(lang.equals("1") ? "ar" : "en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


}
