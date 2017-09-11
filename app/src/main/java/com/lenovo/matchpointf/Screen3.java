package com.lenovo.matchpointf;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static java.security.AccessController.getContext;

/**
 * Activity to display all the tournaments where player can enroll ,the tournament should allow player's category and is only displayed before registration deadline
 * @author atharva vyas
 *
 */

public class Screen3 extends AppCompatActivity {

    public DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference tournaments = rootref.child("tournamentdata");
    public ArrayList<TournamentInfo> tournamentinfo = new ArrayList<>();//collecting list of tournament data
    public String pid,category;
    public float screenWidth, screenHeight;

    public String tournamentname, cityn, venuename;
    public boolean status;
    public long year,month,dayofmonth, year2,month2,dayofmonth2;
    public Date startingDate,registrationDeadline,d;
    public DateFormat datef;
    public String startingDateString,org,registrationDeadlineString;
    public ArrayList<MyClass> enrolled;
    public GenericTypeIndicator<ArrayList<MyClass>> gen;

    public GenericTypeIndicator<CategoryTypes> gen2;
    public CategoryTypes cattypes;
    public TournamentInfo temp;
    @Override
    @Keep
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        getTournaments();
    }

    @Keep
    public void getTournaments() {
        category = getIntent().getStringExtra("CATEGORY");
        Log.i("Category: ",category);

        Query query1 = tournaments.orderByChild("tournamentName");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                    tournamentname = (String) messageSnapshot.child("tournamentName").getValue();
                    cityn = (String) messageSnapshot.child("city").getValue();
                    venuename = (String) messageSnapshot.child("venue").getValue();
                    status=(boolean)messageSnapshot.child("cTypes").child(category).getValue();//checking if the tournament allows this category or not
                     year = (long) messageSnapshot.child("startingDate").child("year").getValue();
                     month = (long) messageSnapshot.child("startingDate").child("month").getValue();
                     dayofmonth = (long) messageSnapshot.child("startingDate").child("date").getValue();
                     startingDate = new GregorianCalendar((int) year + 1900, (int) month, (int) dayofmonth).getTime();
                     datef = new SimpleDateFormat("dd/MM/yyyy", Locale.US);//since we need to display date in readable format
                    startingDateString = datef.format(startingDate);
                    year2 = (long) messageSnapshot.child("registrationDeadline").child("year").getValue();
                     month2 = (long) messageSnapshot.child("registrationDeadline").child("month").getValue();
                    dayofmonth2 = (long) messageSnapshot.child("registrationDeadline").child("date").getValue();

                    gen = new GenericTypeIndicator<ArrayList<MyClass>>() {};
                   enrolled = messageSnapshot.child("enrolled").getValue(gen);

                   gen2 = new GenericTypeIndicator<CategoryTypes>() {};
                   cattypes = messageSnapshot.child("cTypes").getValue(gen2);
                    org=(String) messageSnapshot.child("organizerType").getValue();
                    registrationDeadline = new GregorianCalendar((int) year2 + 1900, (int) month2, (int) dayofmonth2).getTime();
                     d=new Date();

                    registrationDeadlineString = datef.format(registrationDeadline);
                    temp =new TournamentInfo(startingDateString,registrationDeadlineString,tournamentname,org,cityn,venuename,returnType(cattypes),enrolled.size()-1,returnStringCategory(cattypes));
                    if(status && d.before(registrationDeadline))
                        tournamentinfo.add(temp);
                }
                addbuttons();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * The addbuttons method adds the tournament list by adding one tournament at a time to layout
     */
    public void addbuttons() {
        /**
         * layout for each tournament data
         */
        RelativeLayout rls[] = new RelativeLayout[tournamentinfo.size()];
        /**
         * the register button for each tournament layout
         */
        Button my[]=new Button[tournamentinfo.size()];
        /**
         * parent layout for adding tournaments
         */
        LinearLayout ll = (LinearLayout) findViewById(R.id.notificationlayout);
        String st;
        for (int i = 0; i < tournamentinfo.size(); ++i) {
            rls[i] = new RelativeLayout(this);
            TextView tname=new TextView(this);
            LinearLayout pl=new LinearLayout(this);//parent layout containing three linear layouts in itself for proper arrangemennt of data
            pl.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout sl=new LinearLayout(this);//for the side red or yellow bar
            tname.setText(tournamentinfo.get(i).tname);
            tname.setId(100*i+1);
            tname.setTextSize(23);tname.setTextColor(Color.parseColor("#FFFFFF"));
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp1.setMargins(4,2,2,2);
            rls[i].addView(tname,lp1);
            ImageView iv=new ImageView(this);
            iv.setId(100*i+5);
            if(tournamentinfo.get(i).organizer.equals("matchpoint")) {
                iv.setImageResource(R.drawable.logo);
                sl.setBackgroundColor(Color.parseColor("#ff6263"));
            }
            else {
                iv.setImageResource(R.drawable.ext);
                sl.setBackgroundColor(Color.parseColor("#bdbd16"));
            }
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                    (int)(screenWidth/4.5),
                    (int)screenHeight/12);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rls[i].addView(iv,lp2);
            LinearLayout ll1=new LinearLayout(this);
            ll1.setOrientation(LinearLayout.HORIZONTAL);
            TextView tl1=new TextView(this);TextView tl2=new TextView(this);
            tl1.setText("Draw Size:"+tournamentinfo.get(i).drawsize);tl1.setTextColor(Color.parseColor("#FFFFFF"));
            tl2.setText(" Starting Date:"+tournamentinfo.get(i).date);tl2.setTextColor(Color.parseColor("#FFFFFF"));
            LinearLayout.LayoutParams lp11 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp11.setMargins(2, 2, 2, 2);
            ll1.addView(tl1,lp11);
            ll1.addView(tl2,lp11);
            ll1.setId(100*i+2);
            RelativeLayout.LayoutParams lpl1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpl1.addRule(RelativeLayout.BELOW,(100*i+1));
            rls[i].addView(ll1,lpl1);
            LinearLayout ll2=new LinearLayout(this);
            ll2.setOrientation(LinearLayout.HORIZONTAL);
            TextView tl21=new TextView(this);TextView tl22=new TextView(this);
            tl21.setText("Categories:"+tournamentinfo.get(i).category);tl21.setTextColor(Color.parseColor("#FFFFFF"));
            tl22.setText(" Deadline:"+tournamentinfo.get(i).registrationDeadline);tl22.setTextColor(Color.parseColor("#FFFFFF"));
            LinearLayout.LayoutParams lp21 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp21.setMargins(2, 2, 2, 2);
            ll2.addView(tl21,lp21);
            ll2.addView(tl22,lp21);
            ll2.setId(100*i+3);
            RelativeLayout.LayoutParams lpl2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpl2.addRule(RelativeLayout.BELOW,(100*i+2));
            rls[i].addView(ll2,lpl2);
            LinearLayout ll3=new LinearLayout(this);
            ll3.setOrientation(LinearLayout.HORIZONTAL);
            TextView tl31=new TextView(this);TextView tl32=new TextView(this);
            tl31.setText("Location:"+tournamentinfo.get(i).city);tl31.setTextColor(Color.parseColor("#FFFFFF"));
            tl32.setText(" Venue:"+tournamentinfo.get(i).venue);tl32.setTextColor(Color.parseColor("#FFFFFF"));
            LinearLayout.LayoutParams lp31 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp31.setMargins(2, 2, 2, 2);
            ll3.addView(tl31,lp31);
            ll3.addView(tl32,lp31);
            RelativeLayout.LayoutParams lpl3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpl3.addRule(RelativeLayout.BELOW,(100*i+3));
            rls[i].addView(ll3,lpl3);
            my[i]=new Button(this);
            my[i].setText("Register");
            my[i].setTextSize(10);
            my[i].setBackgroundResource(R.drawable.registerbutton);
            final String tname1=tournamentinfo.get(i).tname;
            final String date=tournamentinfo.get(i).date;
            final long drawSize=tournamentinfo.get(i).drawsize;
            final String city=tournamentinfo.get(i).city;
            final String venue=tournamentinfo.get(i).venue;
            final String organizer=tournamentinfo.get(i).organizer;
            final String catString=tournamentinfo.get(i).categoryString;
            my[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonpressed(v,tname1,date,drawSize,city,venue,organizer,catString);
                }
            });
            final float scale = getResources().getDisplayMetrics().density;
            int dps=70;
            int pixels = (int) (dps * scale + 0.5f);//conversion of dp into pixels
            RelativeLayout.LayoutParams lpl4 = new RelativeLayout.LayoutParams(
                    pixels,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpl4.setMargins(4,4,4,4);
            lpl4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lpl4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rls[i].addView(my[i],lpl4);
            rls[i].setBackgroundResource(R.drawable.relativelayoutdesign);
            dps=120;
            pixels = (int) (dps * scale + 0.5f);
            LinearLayout.LayoutParams lpp1 = new LinearLayout.LayoutParams(8,pixels);
            lpp1.setMargins(8,8,8,8);
            pl.addView(sl,lpp1);
            LinearLayout.LayoutParams lpp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,pixels);
            lpp2.setMargins(0, 8, 8, 8);
            pl.addView(rls[i],lpp2);
            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,pixels);
            ll.addView(pl,lpp);
        }
    }

    /**
     * This method is triggered if register button of any tournament bar is clicked
     * passes on essential information to tournament data activity
     * @param view
     * @param tname tournament name
     * @param date
     * @param drawSize
     * @param city
     * @param venue
     * @param organizer
     * @param catString string containing all the categories allowed by tournaments
     */
    public void buttonpressed(View view,String tname,String date,long drawSize,String city,String venue,String organizer,String catString) {
        Intent intent=new Intent(this,TournamentDataActivity.class);
        pid = getIntent().getStringExtra("PLAYER_ID");
        String cat=getIntent().getStringExtra("CATEGORY");
        intent.putExtra("TNAME",tname);
        intent.putExtra("PLAYER_ID",pid);
        intent.putExtra("DATE",date);
        intent.putExtra("DRAWSIZE",String.valueOf(drawSize));
        intent.putExtra("CITY",city);
        intent.putExtra("VENUE",venue);
        intent.putExtra("ORGANIZER",organizer);
        intent.putExtra("CATSTRING",catString);
        intent.putExtra("CATEGORY",cat);
        startActivity(intent);
    }

    /**
     * @param categoryTypes datatype containing information regarding categories allowed by ournament
     * @return 'specific' or 'all categories' according to tournament
     */
    public String returnType(CategoryTypes categoryTypes)
    {
        if(categoryTypes.BU11==false)//could have used if(categoryTypes.BU11 && categoryTypes.BU13 && ...) return "All Junior" else return "Specific"
        {
            return "Specific";
        }
        else if(categoryTypes.GU11==false)
        {
            return "Specific";
        }
        else if(categoryTypes.BU13==false)
        {
            return "Specific";
        }
        else if(categoryTypes.GU13==false)
        {
            return "Specific";
        }
        else if(categoryTypes.BU15==false)
        {
            return "Specific";
        }
        else if(categoryTypes.GU15==false)
        {
            return "Specific";
        }
        else if(categoryTypes.BU17==false)
        {
            return "Specific";
        }
        else if(categoryTypes.GU17==false)
        {
            return "Specific";
        }
        return "All Junior";
    }

    /**
     *
     * @param categoryTypes
     * @return concated string displayin all the allowed categories
     */
    public String returnStringCategory(CategoryTypes categoryTypes)
    {
        String ans="";
        if(categoryTypes.BU11)
        {
            ans+="BU11 ";
        }
        if(categoryTypes.GU11)
        {
            ans+="GU11 ";
        }
        if(categoryTypes.BU13)
        {
            ans+="BU13 ";
        }
        if(categoryTypes.GU13)
        {
            ans+="GU13 ";
        }
        if(categoryTypes.BU15)
        {
            ans+="BU15 ";
        }
        if(categoryTypes.GU15)
        {
            ans+="GU15 ";
        }
        if(categoryTypes.BU17)
        {
            ans+="BU17 ";
        }
        if(categoryTypes.GU17)
        {
            ans+="GU17 ";
        }
        return ans;
    }
}
