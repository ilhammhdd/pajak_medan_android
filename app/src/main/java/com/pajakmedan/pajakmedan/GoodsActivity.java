package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.GoodsAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetGoods;
import com.pajakmedan.pajakmedan.models.Category;
import com.pajakmedan.pajakmedan.models.Goods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 2/19/2018.
 */

public class GoodsActivity extends BaseActivity implements GoodsAdapter.ClickListener {

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
        try {
            Category category = Hawk.get(Constants.CURRENT_CATEGORY_KEY);
            JSONObject request = new JSONObject();
            request.put("url", Constants.DOMAIN + "api/get-goods");
            request.put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY));
            request.put("category_id", category.categoryId);

            JSONObject requestChunk = new JSONObject();
            requestChunk.put("data", request);

            GetGoods getGoods = new GetGoods();
            getGoods.execute(requestChunk);
            getGoods.setOnRequestListener(new GetGoods.OnRequestListener() {
                @Override
                public void onRequest(List<Goods> goodsList) throws JSONException {
                    GoodsAdapter goodsAdapter = new GoodsAdapter(GoodsActivity.this, goodsList);
                    goodsAdapter.setClickListener(GoodsActivity.this);
                    recyclerView_goods.setLayoutManager(new GridLayoutManager(GoodsActivity.this, 2));
                    recyclerView_goods.setAdapter(goodsAdapter);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clickItem(View view, int position, Goods goods) {
        Hawk.put(Constants.CURRENT_GOODS_KEY, goods);
        startActivity(new Intent(GoodsActivity.this, GoodsDetailActivity.class));
    }

    @OnClick(R.id.imageView_goods_back)
    void back(){
        finish();
    }
}
