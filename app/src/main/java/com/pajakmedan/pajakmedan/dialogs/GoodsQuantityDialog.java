package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.Goods;
import com.pajakmedan.pajakmedan.requests.GoodRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

    boolean berhasilTambah;
    MyAsyncTask temp;

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
                GoodRequest goodRequest = new GoodRequest();
                goodRequest.setListener(new ExecuteAsyncTaskListener() {
                    @Override
                    public void onPreExecute(MyAsyncTask myAsyncTask) {
                        temp = myAsyncTask;
                        myAsyncTaskList.add(myAsyncTask);
                    }

                    @Override
                    public void onPostExecute(Object t) {
                        JSONObject responseAll = (JSONObject) t;
                        try {
                            berhasilTambah = responseAll.getBoolean("success");
                            if (!responseAll.getBoolean("success")) {
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject responseData = getResponseData(t);
                        Basket basket = Hawk.get(Constants.BASKET_KEY);
                        try {
                            basket.total = responseData.getInt("basket_total");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Hawk.put(Constants.BASKET_KEY, basket);
                    }
                });
                try {
                    goodRequest.buyGoods(new JSONObject()
                            .put("good_id", goods.goodsId)
                            .put("good_quantity", Integer.parseInt(textViewQuantity.getText().toString()))
                            .put("good_price", goods.goodsPrice));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//                    PostBuyGoods postBuyGoods = new PostBuyGoods(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
//                    postBuyGoods.execute(
//                            new JSONObject()
//                                    .put("good_id", goods.goodsId)
//                                    .put("good_quantity", Integer.parseInt(textViewQuantity.getText().toString()))
//                                    .put("good_price", goods.goodsPrice)
//                    );
//
//                    postBuyGoods.setOnRequestListener(new OnRequestListener() {
//                        @Override
//                        public <T> void onRequest(T responseGeneric, String key) throws JSONException {
//
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                dismiss();
                Toasty.info(GoodsQuantityDialog.this.context, GoodsQuantityDialog.this.context.getResources().getString(R.string.barang_ditambahakan_ke_keranjang), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
