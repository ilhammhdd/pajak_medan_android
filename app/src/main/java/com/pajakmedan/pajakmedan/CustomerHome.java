package com.pajakmedan.pajakmedan;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pajakmedan.pajakmedan.asynctasks.Register;

/**
 * Created by milha on 1/20/2018.
 */

public class CustomerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton toggle_drawer_customer, button_search_customer;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private String TAG = "CUSTOMER_ACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.GOOGLE_API_CLIENT.connect();

        setContentView(R.layout.activity_customer_home);

        drawerLayout = findViewById(R.id.drawer_layout_customer);
        navigationView = findViewById(R.id.navView_customer);
        toggle_drawer_customer = findViewById(R.id.toggle_drawer_customer);
        button_search_customer = findViewById(R.id.button_search_customer);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        toggle_drawer_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        button_search_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchCustomerActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Constants.GOOGLE_API_CLIENT.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.customer_sidenav_menu_basket: {

                break;
            }
            case R.id.customer_sidenav_menu_wishlist: {

                break;
            }
            case R.id.customer_sidenav_menu_notifikasi: {

                break;
            }
            case R.id.customer_sidenav_menu_order_history: {

                break;
            }
            case R.id.customer_sidenav_menu_logout: {
                Log.d(TAG, "Side nav menu button clicked");
                if (Constants.AUTH_TYPE.equals("native")) {
                    Log.d(TAG, "User with native authentication successfully logout");
                    Constants.AUTH_TYPE = "";

                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    finish();
                } else if (Constants.AUTH_TYPE.equals("google")) {
                    Log.d(TAG, "User with google authentication successfully logout");
                    Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                        }
                    });
                    Constants.AUTH_TYPE = "";

                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    finish();
                } else if (Constants.AUTH_TYPE.equals("facebook")) {
                    Log.d(TAG, "User with facebook authentication successfully logout");
                    LoginManager.getInstance().logOut();

                    Constants.AUTH_TYPE = "";

                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    finish();
                }
                break;
            }
        }
        drawerLayout.closeDrawer(Gravity.START);
        return true;
    }
}
