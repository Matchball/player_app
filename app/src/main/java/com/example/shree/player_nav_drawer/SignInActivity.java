package com.example.shree.player_nav_drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SignInActivity extends AppCompatActivity {
    private FirebaseDatabase matchPointDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootref = matchPointDatabase.getReference();
    private DatabaseReference playerpdata = rootref.child("playerpdata");
    private DatabaseReference playertdata = rootref.child("playertdata");
    private DatabaseReference tdata=rootref.child("tournamentdata");
    private DatabaseReference center_names=rootref.child("Center");
    private boolean duplicateid;
    private String playerid;
    private EditText idtext;
    private PlayerPersonalData player;
    private CenterSlot centerslot;
    private PlayerTournamentData playert;
    private EditText nametext, citytext, centertext, passtext,addr,addr_city;
    private DatePicker dobcal, joiningdatecal;
    private String name, center, city, gender = "", password, playercategory, cat,address,city_address;
    private Date dob, joiningdate;
    private int age;
    long vacancydec,v;
    String joiningdate2;
    private ArrayList<IndividualMatchPoint> topPoints;
    Long vac;
    private Intent intent2;
    Spinner spinner;
    private String center3,tcity,center4,slotdate,access1;
    String CenterName,AvailableSlot, accesstype;
    Long vacancy;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //matchPointDatabase.setPersistenceEnabled(false);

    }

    public void signIn(View view) {
        duplicateid = false;
       // intent2 = new Intent(this, Screen3.class);
        nametext = (EditText) findViewById(R.id.nametext);
        idtext = (EditText) findViewById(R.id.playeridtext);
        addr=(EditText) findViewById(R.id.address);
        dobcal = (DatePicker) findViewById(R.id.dobcalendar);
        //joiningdatecal = (DatePicker) findViewById(R.id.joiningcalendar);
        RadioGroup rg = (RadioGroup) findViewById(R.id.genderradiogroup);

        playerid = idtext.getText().toString();
        passtext = (EditText) findViewById(R.id.passwordtext);


        name = nametext.getText().toString();
        address=addr.getText().toString();
        //city_address=addr_city.getText().toString();
        //center = centertext.getText().toString();
        //city = citytext.getText().toString();

        password = passtext.getText().toString().replace(" ","");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dob = new GregorianCalendar(dobcal.getYear(), dobcal.getMonth(), dobcal.getDayOfMonth()).getTime();
        //joiningdate = new GregorianCalendar(joiningdatecal.getYear(), joiningdatecal.getMonth(), joiningdatecal.getDayOfMonth()).getTime();

/*
        *//*int day = joiningdate.getDayOfMonth();
        int month =joiningdate.getMonth()+1;
        int year = joiningdate.getYear();*//*
        int day=joiningdatecal.getDayOfMonth();
        int month=joiningdatecal.getMonth()+1;
        int year=joiningdatecal.getYear();

        joiningdate2 =  Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);*/
        Intent intent=getIntent();
        Bundle bd=intent.getExtras();
        if(bd!=null)
        {
             city=(String) bd.get("C1");
            center=(String) bd.get("C2");
            joiningdate2=(String) bd.get("C3");
            vacancydec=(Long) bd.get("C4");


        }
        Toast.makeText(SignInActivity.this, "getextras : "+city +center +joiningdate2+vacancydec, Toast.LENGTH_LONG).show();
        if (rg.getCheckedRadioButtonId() == R.id.maleradiobutton) {
            gender = "male";
        } else if (rg.getCheckedRadioButtonId() == R.id.femaleradiobutton) {
            gender = "female";
        }
        age = getAge(dob);
        cat = getCategory(age, gender);


        Query query = playerpdata.orderByChild("playerid").equalTo(playerid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() < 1) {

                    player = new PlayerPersonalData(playerid, password, name, dob,address,city_address, city, center, gender, joiningdate2, age, cat);
                    topPoints = new ArrayList<IndividualMatchPoint>();
                    IndividualMatchPoint in;
                    in = new IndividualMatchPoint(Long.parseLong("-1"), cat);
                    topPoints.add(in);
                    playert = new PlayerTournamentData(playerid, 0, 0.01, 0, 0, 0, 0, -1, cat, topPoints, 0,city);
                    String userId = playerpdata.push().getKey();
                    playerpdata.child(userId).setValue(player);
                    String userId1 = playertdata.push().getKey();
                    playertdata.child(userId1).setValue(playert);


                    playerpdata.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Toast.makeText(SignInActivity.this, "Player Data entered successfully", Toast.LENGTH_LONG).show();
                            updateCounter();

                          //  intent2=new Intent(SignInActivity.this,Payment.class);
                            intent2.putExtra("PLAYER_ID", playerid);
                            intent2.putExtra("CATEGORY",cat);
                            startActivity(intent2);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(SignInActivity.this, "Data could not be entered check connection", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(SignInActivity.this, "Id already taken try another", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
 public void updateCounter()
 {
     DatabaseReference center_data=center_names.child(center);
     long p=vacancydec-1;
     center_data.child("vacancy").setValue(p);
     Toast.makeText(getApplicationContext(), "Updated cnt::"+p, Toast.LENGTH_LONG).show();
    // Map<String, > users = new HashMap<String, User>();
   //  center_data.child("vacancy").setValue()
    /* Query query5=centerandslot.orderByChild("CenterName").equalTo(center);
     // Toast.makeText(CenterInfo.this, "getextra:"+city_next+cent+joiningdate2+vac_dec, Toast.LENGTH_LONG).show();
     query5.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             Toast.makeText(SignInActivity.this, "--------- "+dataSnapshot, Toast.LENGTH_LONG).show();

             for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                // slotdate = (String) messageSnapshot.child("Availableslot").getValue();
                 v = (Long) messageSnapshot.child("vacancy").getValue();
                 Toast.makeText(SignInActivity.this, "v::" + v, Toast.LENGTH_LONG).show();
                 // vac=v-1;


                // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 //Query query44 = matchPointDatabase.getReference().child("Center").child(user.getUid()).child("name").setValue("mahesh");

                     *//*String uid = user.getUid();*//*
                   //  Toast.makeText(SignInActivity.this, "query44::" + query44, Toast.LENGTH_LONG).show();

             }

         }


         @Override
         public void onCancelled(DatabaseError databaseError) {}
     });

*/

 }

    public static int getAge(Date dateOfBirth) {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) &&
                (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }

    public static String getCategory(int age, String gender) {
        String category = "";
        if (gender == "male") {
            category += "BU";
        } else {
            category += "GU";
        }
        if (age < 11) {
            category += "11";
        } else if (age < 13) {
            category += "13";
        } else if (age < 15) {
            category += "15";
        } else if (age < 17) {
            category += "17";
        } else if (age < 19) {
            category += "19";
        }
        return category;
    }
}
