package com.example.shree.player_nav_drawer;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class SignUpDetails extends AppCompatActivity {

    private String firstname;

    private String lastname;

    private String email;

    private String city;

    private String center;

    private String date;

    private EditText fullname;

    private EditText mobile;

    private EditText password;

    private EditText confirmpassword;

    private ErrorDialog error;

    private OTPDialog otpdialog;

    private LocalStorage local;

    private Typeface dialogf;

    private String otp;

    private static final String REQUEST_OTP = "https://matchball.org/app/otp.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);

        local = new LocalStorage(getApplicationContext());

        dialogf = Typeface.createFromAsset(getAssets(), "fonts/sul.ttf");

        error = new ErrorDialog();

        otpdialog = new OTPDialog();

        fullname = (EditText) findViewById(R.id.fullname);

        mobile = (EditText) findViewById(R.id.mobile);

        password = (EditText) findViewById(R.id.password);

        confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        getDetails();

        ReadSMS.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

                String message = messageText;

                Toast.makeText(SignUpDetails.this, message, Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getDetails() {

        firstname = getIntent().getStringExtra("First");

        lastname = getIntent().getStringExtra("Last");

        email = getIntent().getStringExtra("Email");

        city = getIntent().getStringExtra("City");

        center = getIntent().getStringExtra("Center");

        date = getIntent().getStringExtra("Date");

        fullname.setText(firstname + " " + lastname);

    }


    public void next(View v) {

        if (fullname.getText().toString().isEmpty()) {

            Toast.makeText(SignUpDetails.this, "Please Enter Your Full Name", Toast.LENGTH_LONG).show();

        } else {

            if (mobile.getText().toString().isEmpty()) {

                Toast.makeText(SignUpDetails.this, "Please Enter Mobile Number", Toast.LENGTH_LONG).show();

            } else {

                if (password.getText().toString().isEmpty()) {

                    Toast.makeText(SignUpDetails.this, "Please Enter Password", Toast.LENGTH_LONG).show();

                } else {

                    if (confirmpassword.getText().toString().isEmpty()) {

                        Toast.makeText(SignUpDetails.this, "Please Confirm Password", Toast.LENGTH_LONG).show();

                    } else {

                        check();

                    }

                }

            }

        }

    }

    public void check() {

        if (mobile.getText().toString().length() != 10) {

            Toast.makeText(SignUpDetails.this, "Please Enter Correct Mobile Number", Toast.LENGTH_LONG).show();

        }

        else {

            if (password.getText().toString().length() < 6) {

                Toast.makeText(SignUpDetails.this, "Password Should Contain Minimum 6 Chars", Toast.LENGTH_LONG).show();

            } else {

                if (password.getText().toString().equals(confirmpassword.getText().toString())) {

                    requestOTP("91" + mobile.getText().toString());

                }

                else {

                    Toast.makeText(SignUpDetails.this, "Password Do Not Match", Toast.LENGTH_LONG).show();

                }

            }

        }

    }


    private void requestOTP(final String mobile) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(SignUpDetails.this, R.style.MyTheme);
                loading.setCancelable(false);
                loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                loading.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if (s.equalsIgnoreCase("")) {

                    error.showDialog(SignUpDetails.this, "Please Try Again!", dialogf);

                } else {

                    if (s.length() == 4) {

                        otp = s;

                        local.resentotpsave(otp);

                        otpdialog.showDialog(SignUpDetails.this, mobile, dialogf);

                    } else {

                        Toast.makeText(SignUpDetails.this, s, Toast.LENGTH_SHORT).show();

                    }

                }

            }


            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("to", params[0]);

                String result = ruc.sendPostRequest(REQUEST_OTP, data);

                return result;

            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(mobile);

    }

}
