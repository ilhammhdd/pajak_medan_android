package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.GoodsAdapter;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Category;
import com.pajakmedan.pajakmedan.models.Goods;
import com.pajakmedan.pajakmedan.requests.GoodRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 2/19/2018.
 */

public class GoodsActivity extends BaseActivity {

    @BindView(R.id.recyclerView_goods)
    RecyclerView recyclerView_goods;
    @BindView(R.id.textView_goods_categoryName)
    TextView textViewCategoryName;

    @Override
    int getContentId() {
        return R.layout.activity_goods;
    }

    @Override
    void insideOnCreate() {
        Constants.GOOGLE_API_CLIENT.connect();
        Category category = Hawk.get(Constants.CURRENT_CATEGORY_KEY);
        textViewCategoryName.setText(category.categoryName);
        showGoods();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void showGoods() {
        Category category = Hawk.get(Constants.CURRENT_CATEGORY_KEY);
        GoodRequest goodRequest = new GoodRequest();
        goodRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPostExecute(Object t) {
                GoodsAdapter goodsAdapter = new GoodsAdapter(GoodsActivity.this, (List<Goods>) t);
                goodsAdapter.setClickListener(new GoodsAdapter.ClickListener() {
                    @Override
                    public void clickItem(View view, int position, Goods goods) {
                        Hawk.put(Constants.CURRENT_GOODS_KEY, goods);
                        startActivity(new Intent(GoodsActivity.this, GoodsDetailActivity.class));
                    }
                });
                recyclerView_goods.setLayoutManager(new GridLayoutManager(GoodsActivity.this, 2));
                recyclerView_goods.setAdapter(goodsAdapter);
            }
        });
        goodRequest.getGoods(category);
    }

    @OnClick(R.id.imageView_goods_back)
    void back() {
        finish();
    }

    @OnClick(R.id.imageView_goods_basket)
    void basket() {
        openBasket(GoodsActivity.this);
    }
}
