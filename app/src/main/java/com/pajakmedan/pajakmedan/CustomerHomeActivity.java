package com.pajakmedan.pajakmedan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.CategoryAdapter;
import com.pajakmedan.pajakmedan.adapters.CategoryAdapter.ClickListener;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Category;
import com.pajakmedan.pajakmedan.models.Profile;
import com.pajakmedan.pajakmedan.requests.CategoryRequest;
import com.pajakmedan.pajakmedan.requests.CustomerRequest;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class CustomerHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

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

    ImageView navHeaderImageViewProfilePhoto;
    TextView navHeaderTextViewProfileName;
    TextView navHeaderTextViewProfileEmail;

    @Override
    int getContentId() {
        return R.layout.activity_customer_home;
    }

    @Override
    void insideOnCreate() {
        Constants.GOOGLE_API_CLIENT.connect();

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        setNavigationValues();
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

    private void setNavigationValues() {
        View headerView = navigationView.getHeaderView(0);
        navHeaderImageViewProfilePhoto = headerView.findViewById(R.id.circleImageView_navHeader_photo);
        navHeaderTextViewProfileName = headerView.findViewById(R.id.textView_navHeader_name);
        navHeaderTextViewProfileEmail = headerView.findViewById(R.id.textView_navHeader_email);

        Profile profile = Hawk.get(Constants.PROFILE_KEY);
        Glide.with(getApplicationContext()).load(String.valueOf(Hawk.get(Constants.PROFILE_PHOTO))).apply(RequestOptions.circleCropTransform()).into(navHeaderImageViewProfilePhoto);
        navHeaderTextViewProfileName.setText(profile.fullName);
        navHeaderTextViewProfileEmail.setText(profile.email);

        navHeaderImageViewProfilePhoto.setOnClickListener(this);
        navHeaderTextViewProfileName.setOnClickListener(this);
        navHeaderTextViewProfileEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circleImageView_navHeader_photo: {
                startActivity(new Intent(CustomerHomeActivity.this, ProfileActivity.class));
                break;
            }
            case R.id.textView_navHeader_name: {
                startActivity(new Intent(CustomerHomeActivity.this, ProfileActivity.class));
                break;
            }
            case R.id.textView_navHeader_email: {
                startActivity(new Intent(CustomerHomeActivity.this, ProfileActivity.class));
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Constants.GOOGLE_API_CLIENT.connect();
        showEvents();
        showCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderLayout_event.startAutoCycle();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.ingin_keluar)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomerHomeActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sliderLayout_event.removeAllSliders();
        sliderLayout_event.stopAutoCycle();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.customer_sidenav_menu_basket: {
                openBasket(CustomerHomeActivity.this);
                break;
            }
            case R.id.customer_sidenav_menu_wishlist: {

                break;
            }
            case R.id.customer_sidenav_menu_notifikasi: {

                break;
            }
            case R.id.customer_sidenav_menu_order_history: {
                startActivity(new Intent(CustomerHomeActivity.this, OrderHistoryActivity.class));
                break;
            }
            case R.id.customer_sidenav_menu_logout: {
                logout(CustomerHomeActivity.this);
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
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void showCategories() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPostExecute(Object t) {
                CategoryAdapter categoryAdapter = new CategoryAdapter(CustomerHomeActivity.this, (List<Category>) t);
                categoryAdapter.setClickListener(CustomerHomeActivity.this);
                recyclerView_category.setAdapter(categoryAdapter);
                recyclerView_category.setLayoutManager(new LinearLayoutManager(CustomerHomeActivity.this, LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }
        });
        categoryRequest.getAllCateogies();
    }

    @Override
    public void clickItem(View view, int position, Category category) {
        Intent intent = new Intent(CustomerHomeActivity.this, GoodsActivity.class);
        Hawk.put(Constants.CURRENT_CATEGORY_KEY, category);
        startActivity(intent);
    }

    public void showEvents() {
        final CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPostExecute(Object t) {
                HashMap<String, String> events = (HashMap<String, String>) t;
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
                sliderLayout_event.setDuration(4000);
                sliderLayout_event.addOnPageChangeListener(CustomerHomeActivity.this);
            }

            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }
        });
        customerRequest.getAllEvents();
    }
}