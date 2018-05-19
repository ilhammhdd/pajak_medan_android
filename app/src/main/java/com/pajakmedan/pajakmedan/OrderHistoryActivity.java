package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pajakmedan.pajakmedan.adapters.OrderHistoryAdapter;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.AdapterItemClickListener;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Order;
import com.pajakmedan.pajakmedan.requests.CheckoutRequest;

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        setAllCheckouts();
    }

    private void setAllCheckouts() {
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }

            @Override
            public void onPostExecute(Object t) {
                OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, (List<Order>) t);
                orderHistoryAdapter.setAdapterItemClickListener(new AdapterItemClickListener() {
                    @Override
                    public <E> void clickItem(View view, int position, E object) {
                        startActivity(new Intent(OrderHistoryActivity.this, OrderGoodsActivity.class));
                    }
                });

                if (t != null) {
                    recyclerViewOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerViewOrderHistory.setAdapter(orderHistoryAdapter);
                }
            }
        });
        checkoutRequest.getIssuedCheckout();
    }

    @OnClick(R.id.imageView_orderHistory_back)
    void backPressed() {
        finish();
    }
}
