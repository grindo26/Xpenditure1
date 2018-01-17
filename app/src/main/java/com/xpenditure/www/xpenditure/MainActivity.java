package com.xpenditure.www.xpenditure;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.xpenditure.www.xpenditure.R.id.calender;

public class MainActivity extends AppCompatActivity {
    public double money;
    public double add;
    public double remove;
    TextView total;
    RelativeLayout mainLayout;
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView total = (TextView) findViewById(R.id.total);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolBar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout , toolBar , R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout , new HomeFragment());
        fragmentTransaction.commit();


        navigationView = (NavigationView) findViewById(R.id.navMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new HomeFragment());
                        fragmentTransaction.commit();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.month:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new MonthFragment());
                        fragmentTransaction.commit();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.year:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new YearFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Yearly");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.categories:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new CategoriesFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Choose Catagories");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.goals:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new GoalsFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Set Goals");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case calender:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new CalenderFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Calender");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.reminder:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new ReminderFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Set Reminders");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.account:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new AccountFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Accounts");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout , new SettingFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Settings");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }




                return false;
            }
        });





    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
