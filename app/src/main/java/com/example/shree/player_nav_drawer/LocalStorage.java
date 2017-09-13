package com.example.shree.player_nav_drawer;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;

public class LocalStorage {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "Aaruush Pref";

    public static final String METHOD = "Method";

    public static final String FIRSTNAME = "r";

    public static final String LASTNAME = "t";

    public static final String EMAIL = "y";

    public static final String STATUS = "s";

    public static final String CITY = "c";

    public static final String CENTER = "ce";

    public static final String DATE = "d";

    public static final String MOBILE = "m";

    public static final String ONETIMEPASSWORD = "otp";


    public LocalStorage(Context context) {

        this._context = context;

        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();

    }

    public void loginSession(String method, String firstname, String lastname, String email, String city, String center, String date, String mobile) {

        editor.putString(METHOD, method);

        editor.putString(FIRSTNAME, firstname);

        editor.putString(LASTNAME, lastname);

        editor.putString(EMAIL, email);

        editor.putString(CITY, city);

        editor.putString(CENTER, center);

        editor.putString(DATE, date);

        editor.putString(MOBILE, mobile);

        editor.commit();

    }

    public void updateStatus(String status) {

        editor.putString(STATUS, status);

        editor.commit();

    }

    public void resentotpsave(String otp) {

        editor.putString(ONETIMEPASSWORD, otp);

        editor.commit();

    }

    public HashMap<String, String> getProfileDetails() {

        HashMap<String, String> name = new HashMap<>();

        name.put(METHOD, pref.getString(METHOD, null));

        name.put(FIRSTNAME, pref.getString(FIRSTNAME, null));

        name.put(LASTNAME, pref.getString(LASTNAME, null));

        name.put(EMAIL, pref.getString(EMAIL, null));

        name.put(CITY, pref.getString(CITY, null));

        name.put(CENTER, pref.getString(CENTER, null));

        name.put(DATE, pref.getString(DATE, null));

        name.put(MOBILE, pref.getString(MOBILE, null));

        return name;

    }

    public HashMap<String, String> getStatus() {

        HashMap<String, String> status = new HashMap<>();

        status.put(STATUS, pref.getString(STATUS, null));

        return status;

    }

    public HashMap<String, String> getOTPDetails() {

        HashMap<String, String> otpheader = new HashMap<String, String>();

        otpheader.put(ONETIMEPASSWORD, pref.getString(ONETIMEPASSWORD, null));

        return otpheader;

    }

    public boolean userProfileIn() {

        return pref.getBoolean(STATUS, false);

    }


}
