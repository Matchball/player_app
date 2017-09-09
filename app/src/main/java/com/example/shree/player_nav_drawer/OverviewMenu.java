package com.example.shree.player_nav_drawer;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.shree.player_nav_drawer.Drawer_Fragments.About_Us;
import com.example.shree.player_nav_drawer.Drawer_Fragments.LeagueTables;
import com.example.shree.player_nav_drawer.Drawer_Fragments.Notifications;
import com.example.shree.player_nav_drawer.Drawer_Fragments.Player_DashBoard;
import com.example.shree.player_nav_drawer.Drawer_Fragments.Player_profile;
import com.example.shree.player_nav_drawer.Drawer_Fragments.Privacy_polocy;
import com.example.shree.player_nav_drawer.Drawer_Fragments.Tournament_data;
import com.example.shree.player_nav_drawer.Drawer_Fragments.Training_cal;
import com.google.firebase.database.Transaction;

public class OverviewMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    String playerid,categ;
    private Boolean exit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_menu);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set the home as default
        Fragment fragment = new Player_profile();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.Content_fragment, fragment)
                .commit();
      playerid=getIntent().getStringExtra("PLAYER_ID");
       categ=getIntent().getStringExtra("CATEGORY");
        Toast.makeText(OverviewMenu.this, "getextra_overview: "+playerid+categ, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FrameLayout fl = (FrameLayout) findViewById(R.id.Content_fragment);
        android.support.v4.app.FragmentManager fragManager = this.getSupportFragmentManager();
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        Fragment frag = fragManager.getFragments().get(count>0?count-1:count);

            if (count == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("Close App?")
                        .setMessage("Do you want to exit?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).show();
                Fragment f=new Player_profile();

        } else {
            super.onBackPressed();
        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment=new Player_DashBoard();
        Bundle b=new Bundle();
        b.putStringArray("putData", new String[]{playerid, categ});


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            fragment=new Player_profile();
            // Handle the camera action
        } else if (id == R.id.nav_tournament_cal) {
            fragment=new Tournament_data();

        } else if (id == R.id.nav_dash) {
        fragment=new Player_DashBoard();

        }

        else if (id == R.id.nav_training_cal) {
fragment=new Training_cal();
        } else if (id == R.id.nav_League) {
fragment=new LeagueTables();
        } else if (id == R.id.nav_notificaion) {
fragment=new Notifications();
        }
        else if (id == R.id.nav_about) {
fragment=new About_Us();
        }
        else if (id == R.id.nav_policy) {
fragment=new Privacy_polocy();
        }else{

            fragment = new Player_profile();
        }
        fragment.setArguments(b);
        FragmentTransaction Ft=getSupportFragmentManager().beginTransaction();
        Ft.replace(R.id.Content_fragment,fragment);

        Ft.addToBackStack(null);
        Ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
