package com.pajakmedan.pajakmedan.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.adapters.BasketGoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.BasketActivityFragmentListener;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.BasketGoods;
import com.pajakmedan.pajakmedan.requests.BasketRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class BasketGoodsFragment extends BaseFragment {

    @BindView(R.id.recyclerView_basket_basketGoods)
    RecyclerView recyclerViewBasketGoods;
    @BindView(R.id.cardView_basket_basketGoods)
    public CardView cardViewBasketGoods;
    @BindView(R.id.imageView_basket_expand)
    ImageView imageViewExpand;

    BasketActivityFragmentListener listener;

    private List<BasketGoods> basketGoodsList;
    boolean expanded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_basket_goods;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageViewExpand.setImageLevel(1);
    }

    @Override
    public void onStart() {
        super.onStart();
        showBasketGoods();
    }

    private void showBasketGoods() {
        Basket basket = Hawk.get(Constants.BASKET_KEY);

        BasketRequest basketRequest = new BasketRequest();

        basketRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPostExecute(Object t) {
                basketGoodsList = (List<BasketGoods>) t;
                BasketGoodsAdapter basketGoodsAdapter = new BasketGoodsAdapter(getActivity(), basketGoodsList);
                basketGoodsAdapter.setClickListener(new BasketGoodsAdapter.ClickListener() {
                    @Override
                    public void clickItem(View view, int position) {
                        Toasty.info(getActivity(), String.valueOf(basketGoodsList.get(position).totalPrice), Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerViewBasketGoods.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                recyclerViewBasketGoods.setAdapter(basketGoodsAdapter);
            }

            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }
        });

        try {
            basketRequest.getBasketGoods(new JSONObject()
                    .put("basket_id", basket.basketId)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imageView_basket_expand)
    void expand() {
        listener.toogleBasketGoodsExpand(expanded, imageViewExpand);
        expanded = !expanded;
    }

    public void setBasketActivityFragmentListener(BasketActivityFragmentListener listener) {
        this.listener = listener;
    }
}
