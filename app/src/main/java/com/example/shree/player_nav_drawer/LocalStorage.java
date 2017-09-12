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


    public LocalStorage(Context context) {

        this._context = context;

        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();

    }

    public void loginSession(String method, String firstname, String lastname, String email) {

        editor.putString(METHOD, method);

        editor.putString(FIRSTNAME, firstname);

        editor.putString(LASTNAME, lastname);

        editor.putString(EMAIL, email);

        editor.commit();

    }

    public HashMap<String, String> getProfileDetails() {

        HashMap<String, String> name = new HashMap<>();

        name.put(METHOD, pref.getString(METHOD, null));

        name.put(FIRSTNAME, pref.getString(FIRSTNAME, null));

        name.put(LASTNAME, pref.getString(LASTNAME, null));

        name.put(EMAIL, pref.getString(EMAIL, null));

        return name;

    }

}
