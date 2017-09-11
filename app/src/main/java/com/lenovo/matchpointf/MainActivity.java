package com.lenovo.matchpointf;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Activity containing login and sign up option
 */
public class MainActivity extends AppCompatActivity {
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference playerpdata = rootref.child("playerpdata");
    private DatabaseReference playertdata = rootref.child("playertdata");
    private static String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable To Get New Local Machine Hash For Facebook Login: getHash();

    }

    public void loginMain(View v) {
        Intent intent;
        intent = new Intent(this, Screen2.class);
        startActivity(intent);
    }

    public void signupMain(View v) {
        Intent intent;
        intent = new Intent(this, CenterInfo.class);
        startActivity(intent);
    }

    public void getHash() {

        try {

            PackageInfo info = getPackageManager().getPackageInfo("com.lenovo.matchpointf", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));

                Toast.makeText(getApplicationContext(), Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();
            }

        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }


}
