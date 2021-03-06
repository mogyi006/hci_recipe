package com.hanson.android.recipe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Logout();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Toast.makeText(getBaseContext(), "Open", Toast.LENGTH_SHORT).show();
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawer.addDrawerListener(toggle);

        toggle.syncState();

    }

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.root_layout, homeFragment, homeFragment.getTag()).commit();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawer.isDrawerOpen(navigationView);
        super.onPrepareOptionsMenu(menu);
        SharedPreferences pref = getSharedPreferences("Login", Activity.MODE_PRIVATE);
        String userID = pref.getString("userID","");


        TextView userid = (TextView)findViewById(R.id.txt_Menu_UserId);
        userid.setText(userID);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager manager = getSupportFragmentManager();

        if (id == R.id.nav_home)
        {
            //connect to home page
            HomeFragment homeFragment = new HomeFragment();
            manager.beginTransaction().replace(R.id.root_layout, homeFragment, homeFragment.getTag()).addToBackStack(null).commit();

        }
        else if (id == R.id.nav_shoppingcart)
        {
            //connect to category page
            ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
            manager.beginTransaction().replace(R.id.root_layout, shoppingCartFragment, shoppingCartFragment.getTag()).addToBackStack(null).commit();

        }
        else if (id == R.id.nav_category)
        {
            //connect to category page
            CategoryFragment categoryFragment = new CategoryFragment();
            manager.beginTransaction().replace(R.id.root_layout, categoryFragment, categoryFragment.getTag()).addToBackStack(null).commit();

        }
        else if (id == R.id.nav_search)
        {
            //connect to search recipes page
            SearchFragment searchFragment = new SearchFragment();
            manager.beginTransaction().replace(R.id.root_layout, searchFragment, searchFragment.getTag()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_add)
        {
            //connect to add recipe page
             Intent intent = new Intent(this, AddRecipeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_info)
        {
            //connect to information page
            InformationFragment informationFragment = new InformationFragment();
            manager.beginTransaction().replace(R.id.root_layout, informationFragment, informationFragment.getTag()).addToBackStack(null).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

/*    private void Logout(){

        SharedPreferences pref = getSharedPreferences("Login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
        editor.commit();
    }*/
}
