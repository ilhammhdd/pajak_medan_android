package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.OrderHistoryAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetIssuedCheckout;
import com.pajakmedan.pajakmedan.listeners.AdapterItemClickListener;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Customer;
import com.pajakmedan.pajakmedan.models.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 3/7/2018.
 */

public class OrderHistoryActivity extends BaseActivity {

    @BindView(R.id.recyclerView_orderHistory)
    RecyclerView recyclerViewOrderHistory;
    @BindView(R.id.constraintLayout_orderHistory_content)
    ConstraintLayout constraintLayoutOrderHistory;

    @Override
    int getContentId() {
        return R.layout.activity_order_history;
    }

    @Override
    void insideOnCreate() {
        GetIssuedCheckout getIssuedCheckout = new GetIssuedCheckout();
        Customer customer = Hawk.get(Constants.CUSTOMER_KEY);
        try {
            getIssuedCheckout.execute(new JSONObject()
                    .put("data", new JSONObject()
                            .put("url", Constants.DOMAIN + "api/get-issued-checkout")
                            .put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY))
                            .put("customer_id", customer.customerId)
                    )
            );
            getIssuedCheckout.setOnRequestListener(new OnRequestListener() {
                @Override
                public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                    OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, (List<Order>) responseGeneric);

                    orderHistoryAdapter.setAdapterItemClickListener(new AdapterItemClickListener() {
                        @Override
                        public <E> void clickItem(View view, int position, E object) {
                            startActivity(new Intent(OrderHistoryActivity.this, OrderGoodsActivity.class));
                        }
                    });

                    if (responseGeneric != null) {
                        Log.d("LOGGING_SOMETHING", responseGeneric.toString());
                        recyclerViewOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
                        recyclerViewOrderHistory.setAdapter(orderHistoryAdapter);
                    }
//                    assert responseGeneric != null;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imageView_orderHistory_back)
    void backPressed() {
        finish();
    }
}
