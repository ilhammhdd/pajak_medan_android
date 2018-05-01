package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.PostBuyGoods;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.Customer;
import com.pajakmedan.pajakmedan.models.Goods;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by milha on 3/10/2018.
 */

public class GoodsQuantityDialog extends BaseDialog {
    ImageView imageViewPlus;
    ImageView imageViewMinus;
    TextView textViewName;
    TextView textViewQuantity;
    Button buttonBuy;

    @Override
    public int getContentId() {
        return R.layout.goods_quantity_dialog;
    }

    @Override
    public void initComponent() {
        buttonBuy = findViewById(R.id.button_goodsQuantity_buy);
        textViewQuantity = findViewById(R.id.textView_goodsQuantity_quantity);
        textViewName = findViewById(R.id.textView_goodsQuantity_name);
        imageViewPlus = findViewById(R.id.imageView_goodsQuantity_plus);
        imageViewMinus = findViewById(R.id.imageView_goodsQuantity_minus);
    }

    public GoodsQuantityDialog(Activity context) {
        super(context);
        Constants.GOODS_QUANTITY = 1;
        super.activity = context;
        textViewQuantity.setText("1");

        imageViewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.GOODS_QUANTITY > 0) {
                    --Constants.GOODS_QUANTITY;
                    textViewQuantity.setText(String.valueOf(Constants.GOODS_QUANTITY));
                }
            }
        });

        imageViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++Constants.GOODS_QUANTITY;
                textViewQuantity.setText(String.valueOf(Constants.GOODS_QUANTITY));
            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Goods goods = Hawk.get(Constants.CURRENT_GOODS_KEY);
                try {
                    PostBuyGoods postBuyGoods = new PostBuyGoods(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
                    postBuyGoods.execute(
                            new JSONObject()
                                    .put("good_id", goods.goodsId)
                                    .put("good_quantity", Integer.parseInt(textViewQuantity.getText().toString()))
                                    .put("good_price", goods.goodsPrice)
                    );

                    postBuyGoods.setOnRequestListener(new OnRequestListener() {
                        @Override
                        public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                            JSONObject responseData = getResponseData(responseGeneric);
                            Basket basket = Hawk.get(Constants.BASKET_KEY);
                            basket.total = responseData.getInt("basket_total");
                            Hawk.put(Constants.BASKET_KEY, basket);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismiss();
                Toasty.info(activity, activity.getResources().getString(R.string.barang_ditambahakan_ke_keranjang), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
