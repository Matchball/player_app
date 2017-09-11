package com.lenovo.matchpointf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toast.makeText(Payment.this, "Payment activity", Toast.LENGTH_LONG).show();
    }
}
