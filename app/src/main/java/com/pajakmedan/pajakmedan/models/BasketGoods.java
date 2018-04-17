package com.pajakmedan.pajakmedan.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 3/1/2018.
 */

public class BasketGoods {
    public int basketGoodsId;
    public Goods goods;
    public Basket basket;
    public int goodsQuantity;
    public int totalPrice;

    public BasketGoods(int basketGoodsId, Goods goods, Basket basket, int goodsQuantity, int totalPrice) {
        this.basketGoodsId = basketGoodsId;
        this.goods = goods;
        this.basket = basket;
        this.goodsQuantity = goodsQuantity;
        this.totalPrice = totalPrice;
    }

    public static List<BasketGoods> getCheckoutBasketGoods(JSONArray basketGoods) throws JSONException {
        List<BasketGoods> basketGoodsList = new ArrayList<>();

        for (int i = 0; i < basketGoods.length(); i++) {
            basketGoodsList.add(
                    new BasketGoods(
                            basketGoods.getJSONObject(i).getInt("basket_goods_id"),
                            new Goods(
                                    basketGoods.getJSONObject(i).getInt("goods_id"),
                                    basketGoods.getJSONObject(i).getString("goods_image_url"),
                                    basketGoods.getJSONObject(i).getString("goods_name"),
                                    basketGoods.getJSONObject(i).getInt("goods_price"),
                                    basketGoods.getJSONObject(i).getString("goods_unit"),
                                    basketGoods.getJSONObject(i).getInt("goods_available") == 1,
                                    basketGoods.getJSONObject(i).getInt("goods_min_order"),
                                    basketGoods.getJSONObject(i).getString("goods_condition")
                            ),
                            new Basket(
                                    basketGoods.getJSONObject(i).getInt("baskets_id"),
                                    basketGoods.getJSONObject(i).getInt("baskets_customer_id"),
                                    basketGoods.getJSONObject(i).getInt("baskets_total"),
                                    basketGoods.getJSONObject(i).getString("baskets_description"),
                                    basketGoods.getJSONObject(i).getString("basket_status_name")
                            ),
                            basketGoods.getJSONObject(i).getInt("baskets_goods_quantity"),
                            basketGoods.getJSONObject(i).getInt("baskets_goods_total_price")
                    )
            );
        }

        return basketGoodsList;
    }
}
