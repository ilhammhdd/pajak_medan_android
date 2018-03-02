package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.CategoryAdapter;
import com.pajakmedan.pajakmedan.adapters.CategoryAdapter.ClickListener;
import com.pajakmedan.pajakmedan.asynctasks.GetCategory;
import com.pajakmedan.pajakmedan.asynctasks.GetEvent;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.Category;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class CustomerHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

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
    @BindView(R.id.slider_event)
    SliderLayout sliderLayout_event;

    private String TAG = "CUSTOMER_ACTIVITY";

    @Override
    int getContentId() {
        return R.layout.activity_customer_home;
    }

    @Override
    void insideOnCreate() {
        Constants.GOOGLE_API_CLIENT.connect();

        showEvents();
        showCategories();

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
                Intent intent = new Intent(CustomerHomeActivity.this, SearchCustomerActivity.class);
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
        sliderLayout_event.stopAutoCycle();
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
                startActivity(new Intent(CustomerHomeActivity.this, UploadFileActivity.class));
                finish();

                break;
            }

            case R.id.customer_sidenav_menu_basket: {
                Basket basket = Hawk.get(Constants.BASKET_KEY);
                if (basket != null) {
                    startActivity(new Intent(CustomerHomeActivity.this, BasketActivity.class));
                    break;
                }

                startActivity(new Intent(CustomerHomeActivity.this, UploadFileActivity.class));
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
                if (Constants.AUTH_TYPE == 0) {
                    Log.d(TAG, "User with native authentication successfully logout");
                    Constants.AUTH_TYPE = -1;

                    startActivity(new Intent(CustomerHomeActivity.this, RegisterActivity.class));
                    finish();
                } else if (Constants.AUTH_TYPE == 1) {
                    Log.d(TAG, "User with google authentication successfully logout");
                    Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                        }
                    });
                    Constants.AUTH_TYPE = -1;

                    startActivity(new Intent(CustomerHomeActivity.this, RegisterActivity.class));
                    finish();
                } else if (Constants.AUTH_TYPE == 2) {
                    Log.d(TAG, "User with facebook authentication successfully logout");
                    AccessToken.setCurrentAccessToken(null);
                    LoginManager.getInstance().logOut();

                    Constants.AUTH_TYPE = -1;

                    startActivity(new Intent(CustomerHomeActivity.this, RegisterActivity.class));
                    finish();
                }
                break;
            }
        }
        drawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void showCategories() {
        try {
            JSONObject request = new JSONObject();
            request.put("url", Constants.DOMAIN + "api/get-categories");
            request.put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY));

            JSONObject requestChunk = new JSONObject();
            requestChunk.put("data", request);

            GetCategory getCategory = new GetCategory();
            getCategory.execute(requestChunk);
            getCategory.setOnRequestListener(new GetCategory.OnRequestListener() {
                @Override
                public void onRequest(List<Category> categories) throws JSONException {
                    CategoryAdapter categoryAdapter = new CategoryAdapter(CustomerHomeActivity.this, categories);
                    categoryAdapter.setClickListener(CustomerHomeActivity.this);
                    recyclerView_category.setAdapter(categoryAdapter);
                    recyclerView_category.setLayoutManager(new LinearLayoutManager(CustomerHomeActivity.this, LinearLayoutManager.VERTICAL, false));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clickItem(View view, int position, Category category) {
        Intent intent = new Intent(CustomerHomeActivity.this, GoodsActivity.class);
        Hawk.put(Constants.CURRENT_CATEGORY_KEY, category);
        startActivity(intent);
    }

    public void showEvents() {
        try {
            JSONObject request = new JSONObject();
            request.put("url", Constants.DOMAIN + "api/get-events");
            request.put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY));

            JSONObject requestChunk = new JSONObject();
            requestChunk.put("data", request);

            GetEvent getEvent = new GetEvent();
            getEvent.execute(requestChunk);
            Log.d(TAG, "Event request sent");
            getEvent.setOnRequestListener(new GetEvent.OnRequestListener() {
                @Override
                public void onRequest(HashMap<String, String> events) throws JSONException {
                    for (String name : events.keySet()) {
                        TextSliderView textSliderView = new TextSliderView(CustomerHomeActivity.this);

                        textSliderView.description(name).image(events.get(name)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(CustomerHomeActivity.this);
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("extra", name);
                        sliderLayout_event.addSlider(textSliderView);
                    }

                    sliderLayout_event.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    sliderLayout_event.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    sliderLayout_event.setCustomAnimation(new DescriptionAnimation());
                    sliderLayout_event.setDuration(2000);
                    sliderLayout_event.addOnPageChangeListener(CustomerHomeActivity.this);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}