package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.pajakmedan.pajakmedan.asynctasks.GetCategory;
import com.pajakmedan.pajakmedan.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ClickListener {

    @BindView(R.id.recyclerView_category)
    RecyclerView recyclerView_category;
    @BindView(R.id.drawer_layout_customer)
    DrawerLayout drawerLayout;
    @BindView(R.id.navView_customer)
    NavigationView navigationView;
    @BindView(R.id.toggle_drawer_customer)
    ImageButton toggle_drawer_customer;
    @BindView(R.id.button_search_customer)
    ImageButton button_search_customer;

    private String TAG = "CUSTOMER_ACTIVITY";
    List<Category> categoriesList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.GOOGLE_API_CLIENT.connect();
        setContentView(R.layout.activity_customer_home);
        ButterKnife.bind(this);

        CategoryAdapter categoryAdapter = new CategoryAdapter(CustomerHome.this, getCategories());
        categoryAdapter.setClickListener(this);
        recyclerView_category.setAdapter(categoryAdapter);
        recyclerView_category.setLayoutManager(new LinearLayoutManager(CustomerHome.this));

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
                Intent intent = new Intent(CustomerHome.this, SearchCustomerActivity.class);
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
            case R.id.customer_sidenav_menu_upload_file: {
                startActivity(new Intent(CustomerHome.this, UploadFileActivity.class));
                finish();

                break;
            }

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
                    Constants.AUTH_TYPE = null;

                    startActivity(new Intent(CustomerHome.this, RegisterActivity.class));
                    finish();
                } else if (Constants.AUTH_TYPE.equals("google")) {
                    Log.d(TAG, "User with google authentication successfully logout");
                    Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                        }
                    });
                    Constants.AUTH_TYPE = null;

                    startActivity(new Intent(CustomerHome.this, RegisterActivity.class));
                    finish();
                } else if (Constants.AUTH_TYPE.equals("facebook")) {
                    Log.d(TAG, "User with facebook authentication successfully logout");
                    LoginManager.getInstance().logOut();

                    Constants.AUTH_TYPE = null;

                    startActivity(new Intent(CustomerHome.this, RegisterActivity.class));
                    finish();
                }
                break;
            }
        }
        drawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    public List<Category> getCategories() {
//        categories.clear();
//
//        categories.add(new Category("https://developer.android.com/_static/images/android/touchicon-180.png", "Android"));
//        categories.add(new Category("http://www.loghouselife.com/wp-content/uploads/ngg_featured/Answering-the-How-can-NLP-help-me-question.png", "NLP"));
//        categories.add(new Category("https://i.gadgets360cdn.com/large/AI_1476432726050.jpeg", "AI"));
//        categories.add(new Category("https://www.apple.com/ios/images/og.png?201711061438", "iOS"));
//        categories.add(new Category("https://avatars0.githubusercontent.com/u/379109?s=400&v=4", "Arduino"));
//        categories.add(new Category("http://www.rai.it/dl/img/2016/10/01/1475345205691_www_image.jpg", "Website"));
//
//        return categories;

        try {
            JSONObject request = new JSONObject();
            request.put("url", Constants.DOMAIN + "api/get-categories");

            JSONObject requestChunk = new JSONObject();
            requestChunk.put("data", request);

            GetCategory getCategory = new GetCategory();
            getCategory.execute(requestChunk);
            getCategory.setOnRequestListener(new GetCategory.OnRequestListener() {
                @Override
                public void onRequest(List<Category> categories) throws JSONException {
                    categoriesList = categories;
                }
            });

            /*
            *
            * MASALAHNYA DISINI
            *
            * */

            Log.d("TOOOL", "categoriesList below : ");

            for (int i = 0; i < categoriesList.size(); i++) {
                Log.d("TOOOL", categoriesList.get(i).categoryName);
                Log.d("TOOOL", categoriesList.get(i).categoryImageUrl);
            }

            return categoriesList;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void clickItem(View view, int position) {
        startActivity(new Intent(CustomerHome.this, UploadFileActivity.class));
    }
}