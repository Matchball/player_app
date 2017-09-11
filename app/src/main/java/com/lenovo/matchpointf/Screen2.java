package com.lenovo.matchpointf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.text.TextUtils.isEmpty;

/**
 * Activity for login by the user
 *
 * @author atharva vyas
 */
public class Screen2 extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Google
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;

    // Facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    // References for accessing firebase database
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference playerpdata = rootref.child("playerpdata");

    private Intent intent;
    private String playerid, category, passwd;

    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        requestWindowFeature(Window.FEATURE_RIGHT_ICON);
        setContentView(R.layout.activity_screen2);

        // Initialising the layout
        layoutInit();

        // initialising firebase
        firebaseInit();
        googleInit();
        facebookInit();
    }

//    public void hideKeyboard() {
//
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//
//        EditText username_et = (EditText) findViewById(R.id.username);
//        EditText password_et = (EditText) findViewById(R.id.password);
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(username_et.getWindowToken(), 0);
//        imm.hideSoftInputFromWindow(password_et.getWindowToken(), 0);
//    }

    public void layoutInit() {
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText(Html.fromHtml(getString(R.string.sign_up_link)));

        // layout should not move as soft keyboard opens
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Hide the keyboard
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void firebaseInit() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(Screen2.this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void googleInit() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(Screen2.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        setGooglePlusButtonText(signInButton, "Continue with Google");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void facebookInit() {
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

    protected void setGooglePlusButtonText(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            return;
        }

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Display Name: " + acct.getDisplayName(), Toast.LENGTH_SHORT).show();

            firebaseAuthWithGoogle(acct);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Screen2.this, "Authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(Screen2.this, OverviewActivity.class));
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(Screen2.this, "An account already exists with the same email address.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Screen2.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            startActivity(new Intent(Screen2.this, OverviewActivity.class));
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * checks if login id and password matches
     */
    public void loginScreen(View v) {
        /*progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setIndeterminate(true);
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();*/
        /*ProgressDialog pd = new ProgressDialog(this,R.style.MyTheme);*/
        progress =new ProgressDialog(this,R.style.MyTheme);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Logging in. Please wait.");
        progress.show();
//        ProgressDialog dialog = ProgressDialog.show(Screen2.this, "Plz wait", "Wait while loading...", true);
        progress.setCancelable(true);



        intent = new Intent(this, OverviewActivity.class);
        Log.i("Log","overview dismiss");
        playerid = ((EditText) (findViewById(R.id.logintext))).getText().toString();
        passwd = ((EditText) (findViewById(R.id.passtext))).getText().toString();
        if(playerid.isEmpty())
        {
            Toast.makeText(Screen2.this, "Please enter username", Toast.LENGTH_LONG).show();
            //  return;
        }else if(passwd.isEmpty()){Toast.makeText(Screen2.this, "Please enter password", Toast.LENGTH_LONG).show();}
           // else{Toast.makeText(Screen2.this, "Please enter email and password", Toast.LENGTH_LONG).show();}
        Query query = playerpdata.orderByChild("playerid").equalTo(playerid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                    String tpasswd = (String) messageSnapshot.child("password").getValue();
                    category = (String) messageSnapshot.child("playercategory").getValue();
                   // Toast.makeText(Screen2.this, "login equal tpasswd" + tpasswd + " passwd " + passwd, Toast.LENGTH_LONG).show();

                    if (!tpasswd.isEmpty()& tpasswd.equals(passwd)) {
                        //Toast.makeText(Screen2.this, "login succesful", Toast.LENGTH_LONG).show();
                        intent.putExtra("PLAYER_ID", playerid);
                        intent.putExtra("CATEGORY", category);
                        startActivity(intent);
                        progress.dismiss();
                        Log.i("Log","Data Change");
                    }
                    else if (!tpasswd.isEmpty()& !tpasswd.equals(passwd)){
                        Toast.makeText(Screen2.this, "You have entered wrong password.. ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Log.i("Log","End of login");
    }
}
