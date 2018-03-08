package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.models.Category;
import com.pajakmedan.pajakmedan.models.Goods;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 2/23/2018.
 */

public class GoodsDetailActivity extends BaseActivity {

    @BindView(R.id.textView_detailGoods_availibility)
    TextView textViewAvailibility;
    @BindView(R.id.imageView_detailGoods_starImage)
    ImageView imageViewStar;
    @BindView(R.id.imageView_detailGoods_image)
    ImageView imageViewGoods;
    @BindView(R.id.textView_detailGoods_name)
    TextView textViewGoodsName;
    @BindView(R.id.textView_detailGoods_price)
    TextView textViewGoodsPrice;
    @BindView(R.id.textView_detailGoods_satuan)
    TextView textViewGoodsSatuan;
    @BindView(R.id.textView_detailGoods_minPemesanan)
    TextView textViewGoodsMinPemesanan;
    @BindView(R.id.textView_detailGoods_kondisi)
    TextView textViewGoodsKondisi;
    @BindView(R.id.textView_detailGoods_kategori)
    TextView textViewGoodsKategori;
    @BindView(R.id.textView_detailGoods_goodsName1)
    TextView textViewGoodsName1;

    @Override
    int getContentId() {
        return R.layout.activity_detail_goods;
    }

    @Override
    void insideOnCreate() {
        imageViewStar.getLayoutParams().width = Constants.DEVICE_WIDTH / 10;
        imageViewStar.getLayoutParams().height = Constants.DEVICE_HEIGHT / 16;
        setAllComponent((Goods) Hawk.get(Constants.CURRENT_GOODS_KEY));
        Constants.GOOGLE_API_CLIENT.connect();
        Goods goods = Hawk.get(Constants.CURRENT_GOODS_KEY);
        textViewGoodsName1.setText(goods.goodsName);
    }

    private void setAllComponent(Goods goods) {
        String minPemesanan = goods.minOrder + " " + goods.goodsUnit;
        String tersedia;
        if (goods.goodsAvalibility) {
            tersedia = getResources().getString(R.string.tersedia);
            textViewAvailibility.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            textViewAvailibility.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            tersedia = getResources().getString(R.string.tidak_tersedia);
            textViewAvailibility.setBackgroundColor(getResources().getColor(R.color.colorRed));
            textViewAvailibility.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        Category category = Hawk.get(Constants.CURRENT_CATEGORY_KEY);

        Glide.with(getApplicationContext()).load(goods.goodsImageUrl).into(imageViewGoods);
        imageViewStar.setImageLevel(0);
        textViewGoodsName.setText(goods.goodsName);
        textViewGoodsPrice.setText(String.valueOf(goods.goodsPrice));
        textViewAvailibility.setText(tersedia);
        textViewGoodsSatuan.setText(goods.goodsUnit);
        textViewGoodsMinPemesanan.setText(minPemesanan);
        textViewGoodsKondisi.setText(goods.condition);
        textViewGoodsKategori.setText(category.categoryName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @OnClick(R.id.imageView_goods_back1)
    void back() {
        finish();
    }

    @OnClick(R.id.imageView_detailGoods_basket)
    void basket() {
        startActivity(new Intent(GoodsDetailActivity.this, BasketActivity.class));
    }
}
