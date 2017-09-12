package com.example.shree.player_nav_drawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpDetails extends AppCompatActivity {

    private String firstname;

    private String lastname;

    private EditText fullname;

    private EditText mobile;

    private EditText password;

    private EditText confirmpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);

        fullname = (EditText) findViewById(R.id.fullname);

        mobile = (EditText) findViewById(R.id.mobile);

        password = (EditText) findViewById(R.id.password);

        confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        getDetails();

    }

    private void getDetails() {

        firstname = getIntent().getStringExtra("First");

        lastname = getIntent().getStringExtra("Last");

        fullname.setText(firstname + " " + lastname);

    }


    public void next(View v) {

        if (fullname.getText().toString().isEmpty()) {

            Toast.makeText(SignUpDetails.this, "Please Enter Your Full Name", Toast.LENGTH_LONG).show();

        }

        else if (mobile.getText().toString().isEmpty()) {

            Toast.makeText(SignUpDetails.this, "Please Enter Password", Toast.LENGTH_LONG).show();

        }

        else if (password.getText().toString().isEmpty()) {

            Toast.makeText(SignUpDetails.this, "Please Enter Password", Toast.LENGTH_LONG).show();

        }

        else if (confirmpassword.getText().toString().isEmpty()) {

            Toast.makeText(SignUpDetails.this, "Please Confirm Password", Toast.LENGTH_LONG).show();

        }

        check();

    }

    public void check() {

        if (password.getText().toString() == confirmpassword.getText().toString()) {

            Toast.makeText(SignUpDetails.this, "Under Development!", Toast.LENGTH_LONG).show();

        }

        else {

            Toast.makeText(SignUpDetails.this, "Passwords Do Not Match!", Toast.LENGTH_LONG).show();

        }

    }

}
