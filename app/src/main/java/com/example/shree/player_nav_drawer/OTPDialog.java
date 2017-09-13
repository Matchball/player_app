package com.example.shree.player_nav_drawer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPDialog {

    private static final String REQUEST_OTP = "https://matchball.org/app/otp.php";

    private static final String REQUEST_VERIFIED = "http://rishabhs.me/verified.php";

    public static final String OTP_REGEX = "[0-9]{1,6}";

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private String first;

    private String second;

    private String third;

    private String four;

    private String otp;

    private String smsotp;

    private String onetimepassword;

    private String requestmobile;

    private Button dialogButton;

    public TextView text;

    public TextView number;

    static EditText otpfieldone;

    static EditText otpfieldtwo;

    static EditText otpfieldthree;

    static EditText otpfieldfour;

    private TextView timer;

    private TextView resend;

    private ImageView imagetimer;

    private LocalStorage session;

    private Activity from;

    private ErrorDialog error;

    private Typeface usefont;


    public void showDialog(final Activity activity, final String mobile, Typeface font) {

        final Dialog dialog = new Dialog(activity);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.otpdialog);

        requestmobile = mobile;

        from = activity;

        usefont = font;

        error = new ErrorDialog();

        otpfieldone = (EditText) dialog.findViewById(R.id.o1);

        otpfieldtwo = (EditText) dialog.findViewById(R.id.o2);

        otpfieldthree = (EditText) dialog.findViewById(R.id.o3);

        otpfieldfour = (EditText) dialog.findViewById(R.id.o4);

        session = new LocalStorage(activity);

        timer = (TextView) dialog.findViewById(R.id.timer);

        imagetimer = (ImageView) dialog.findViewById(R.id.imagetimer);

        resend = (TextView) dialog.findViewById(R.id.resend);

        text = (TextView) dialog.findViewById(R.id.text_dialog);

        number = (TextView) dialog.findViewById(R.id.otpnumber);

        textWatcher();

        countdown();

        text.setTypeface(font);

        number.setTypeface(font);

        timer.setTypeface(font);

        resend.setTypeface(font);

        number.setText("+91 " + mobile.substring(2));

        dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);

        dialogButton.setEnabled(false);

        dialogButton.setTextColor(Color.parseColor("#ff9e9e9e"));

        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                check("submit");

            }

        });

        resend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                check("resend");

            }

        });

        dialog.show();

        firebaseInit();

        receivedSms();

    }


    public void firebaseInit() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    Toast.makeText(from, "Invalid Login", Toast.LENGTH_SHORT).show();

                    session.updateStatus(null);

                    Intent go = new Intent(from, LoginActivity.class);

                    from.startActivity(go);

                    from.finish();

                }

            }
        };

    }


    public void countdown() {

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                timer.setText("Seconds Remaining: " + millisUntilFinished / 1000);

            }

            public void onFinish() {

                imagetimer.setVisibility(View.GONE);

                timer.setVisibility(View.GONE);

                resend.setVisibility(View.VISIBLE);

                resend.setText("Didn't Get OTP? Request Another");

            }

        }.start();

    }


    public void check(String prompt) {

        if (prompt.equalsIgnoreCase("submit")) {

            verification();

        } else if (prompt.equalsIgnoreCase("resend")) {

            requestOTP(requestmobile);

        }


    }


    public void receivedSms() {

        ReadSMS.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

                Pattern pattern = Pattern.compile(OTP_REGEX);

                Matcher matcher = pattern.matcher(messageText);

                while (matcher.find()) {

                    smsotp = matcher.group();

                }

                otpfieldone.setText(smsotp.substring(0));

                otpfieldtwo.setText(smsotp.substring(1));

                otpfieldthree.setText(smsotp.substring(2));

                otpfieldfour.setText(smsotp.substring(3));

            }
        });

    }


    private void verification() {

        HashMap<String, String> rotp = session.getOTPDetails();

        onetimepassword = rotp.get(LocalStorage.ONETIMEPASSWORD);

        first = otpfieldone.getText().toString();

        second = otpfieldtwo.getText().toString();

        third = otpfieldthree.getText().toString();

        otp = first + second + third + four;

        if (otp.equals(onetimepassword)) {

            verify(requestmobile.substring(2));

        } else {

            error.showDialog(from, "Incorrect OTP", usefont);

        }

    }


    private void textWatcher() {

        otpfieldone.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

                if (otpfieldone.getText().toString().length() == 2) {

                    String o = otpfieldone.getText().toString();

                    otpfieldtwo.setText(o.substring(1));

                    otpfieldtwo.setEnabled(true);

                    otpfieldtwo.setSelection(otpfieldtwo.getText().length());

                    otpfieldtwo.requestFocus();

                    String text = otpfieldone.getText().toString();

                    otpfieldone.setText(text.substring(0, text.length() - 1));

                }

            }

        });

        otpfieldtwo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

                if (otpfieldtwo.getText().toString().length() == 0) {

                    otpfieldone.setSelection(otpfieldone.getText().length());

                    otpfieldone.requestFocus();

                    otpfieldtwo.setEnabled(false);

                }

                if (otpfieldtwo.getText().toString().length() == 2) {

                    String t = otpfieldtwo.getText().toString();

                    otpfieldthree.setText(t.substring(1));

                    otpfieldthree.setEnabled(true);

                    otpfieldthree.setSelection(otpfieldthree.getText().length());

                    otpfieldthree.requestFocus();

                    String lol = otpfieldtwo.getText().toString();

                    otpfieldtwo.setText(lol.substring(0, lol.length() - 1));

                }

            }

        });

        otpfieldthree.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

                if (otpfieldthree.getText().toString().length() == 0) {

                    otpfieldtwo.setSelection(otpfieldtwo.getText().length());

                    otpfieldtwo.requestFocus();

                    otpfieldthree.setEnabled(false);

                }

                if (otpfieldthree.getText().toString().length() == 2) {

                    String t = otpfieldthree.getText().toString();

                    otpfieldfour.setText(t.substring(1));

                    otpfieldfour.setEnabled(true);

                    otpfieldfour.setSelection(otpfieldfour.getText().length());

                    otpfieldfour.requestFocus();

                    String text = otpfieldthree.getText().toString();

                    otpfieldthree.setText(text.substring(0, text.length() - 1));

                }

            }

        });

        otpfieldfour.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                four = otpfieldfour.getText().toString();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

                if (otpfieldfour.getText().toString().length() == 0) {

                    otpfieldthree.setSelection(otpfieldthree.getText().length());

                    otpfieldthree.requestFocus();

                    otpfieldfour.setEnabled(false);

                    dialogButton.setEnabled(false);

                    dialogButton.setTextColor(Color.parseColor("#ff9e9e9e"));
                }

                if (otpfieldfour.getText().toString().length() == 1) {

                    dialogButton.setEnabled(true);

                    dialogButton.setTextColor(Color.parseColor("#CC000000"));

                }

            }

        });

    }


    private void requestOTP(final String mobile) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(from, R.style.MyTheme);
                loading.setCancelable(false);
                loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                loading.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if (s.equalsIgnoreCase("")) {

                    error.showDialog(from, "Please Try Again!", usefont);

                } else {

                    if (s.length() == 4) {

                        resend.setText("OTP Requested!");

                        session.resentotpsave(s);

                    } else {

                        Toast.makeText(from, s, Toast.LENGTH_SHORT).show();

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

    private void signupOnFirebase(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential).addOnCompleteListener(from, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                        Toast.makeText(from, "An account already exists with the same email address.", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(from, "Authentication failed.", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    dialogButton.setText("Verified");

                    session.updateStatus("Logged");

                    Intent intent = new Intent(from, OverviewMenu.class);

                    from.startActivity(intent);

                    from.finish();

                }

            }
        });

    }



    private void verify(final String mobile) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialogButton.setText("Verifying");

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.equalsIgnoreCase("")) {

                    dialogButton.setText("Submit");

                    error.showDialog(from, "Failed. Try Again", usefont);

                    return;

                }

                if (s.contains("Mobile Verified Successfully!")) {

                    signupOnFirebase(AccessToken.getCurrentAccessToken());

                    return;

                } else {

                    error.showDialog(from, s, usefont);

                    return;

                }

            }


            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("mobile", params[0]);

                String result = ruc.sendPostRequest(REQUEST_VERIFIED, data);

                return result;

            }
        }

        RegisterUser ru = new RegisterUser();

        ru.execute(mobile);

    }

}