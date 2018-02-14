package com.pajakmedan.pajakmedan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pajakmedan.pajakmedan.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 2/12/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Category> categories = new ArrayList<>();
    private Context context;
    static ClickListener clickListener;

    CategoryAdapter(Context context, List<Category> categories) {
        this.layoutInflater = LayoutInflater.from(context);
        this.categories = categories;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category currentCategory = categories.get(position);
        holder.textView_categoryName.setText(currentCategory.categoryName);
        if (categories.get(position).categoryImageUrl != null) {
            Glide.with(context).load(categories.get(position).categoryImageUrl).into(holder.imageView_category);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    void setClickListener(ClickListener clickListener) {
        CategoryAdapter.clickListener = clickListener;
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView textView_categoryName;
    ImageView imageView_category;

    CategoryViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        textView_categoryName = itemView.findViewById(R.id.category_name);
        imageView_category = itemView.findViewById(R.id.category_image);
    }

    @Override
    public void onClick(View view) {
        CategoryAdapter.clickListener.clickItem(view, getAdapterPosition());
    }
}

interface ClickListener {
    void clickItem(View view, int position);
}