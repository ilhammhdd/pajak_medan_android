package com.pajakmedan.pajakmedan.models;

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
}
