package com.xpenditure.www.xpenditure;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import static com.xpenditure.www.xpenditure.R.id.calender;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mtoggel;
    Toolbar toolbar;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navMenu);

        mtoggel = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(mtoggel);
        mtoggel.syncState();


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Xpenditure");
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
                        fragmentTransaction.replace(R.id.frameLayout , new LoginFragment());
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




                return true;
            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggel.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
