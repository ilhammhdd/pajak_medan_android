package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.BasketActivity;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.adapters.BasketGoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetBasketGoods;
import com.pajakmedan.pajakmedan.listeners.BasketActivityFragmentListener;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.BasketGoods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasketGoodsFragment extends Fragment {

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
        showBasketGoods();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket_goods, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageViewExpand.setImageLevel(1);
    }

    private void showBasketGoods() {
        try {
            Basket basket = Hawk.get(Constants.BASKET_KEY);

            GetBasketGoods getBasketGoods = new GetBasketGoods(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
            getBasketGoods.execute(new JSONObject()
                    .put("basket_id", basket.basketId)
            );

            getBasketGoods.setOnRequestListener(new OnRequestListener() {
                @Override
                public <T> void onRequest(T basketGoods, String key) {
                    basketGoodsList = (List<BasketGoods>) basketGoods;
                    BasketGoodsAdapter basketGoodsAdapter = new BasketGoodsAdapter(getActivity(), basketGoodsList);
                    basketGoodsAdapter.setClickListener(new BasketGoodsAdapter.ClickListener() {
                        @Override
                        public void clickItem(View view, int position) {
                            Toast.makeText(getActivity(), String.valueOf(basketGoodsList.get(position).totalPrice), Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerViewBasketGoods.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    recyclerViewBasketGoods.setAdapter(basketGoodsAdapter);
                }
            });
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
