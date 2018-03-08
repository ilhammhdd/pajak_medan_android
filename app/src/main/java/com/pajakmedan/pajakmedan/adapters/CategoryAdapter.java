package com.pajakmedan.pajakmedan.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Category> categories = new ArrayList<>();
    private Context context;
    static ClickListener clickListener;

    public CategoryAdapter(Context context, List<Category> categories) {
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
        holder.layoutCategory.getLayoutParams().height = Constants.DEVICE_HEIGHT / 7;
        if (categories.get(position).categoryImageUrl != null) {
            Glide.with(context).load(categories.get(position).categoryImageUrl).into(holder.imageView_category);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_categoryName;
        ImageView imageView_category;
        ConstraintLayout layoutCategory;

        CategoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView_categoryName = itemView.findViewById(R.id.category_name);
            imageView_category = itemView.findViewById(R.id.category_image);
            layoutCategory = itemView.findViewById(R.id.layout_category_item);
        }

        @Override
        public void onClick(View view) {
            CategoryAdapter.clickListener.clickItem(view, getAdapterPosition(), categories.get(getAdapterPosition()));
        }
    }

    public interface ClickListener {
        void clickItem(View view, int position, Category category);
    }

    public void setClickListener(ClickListener clickListener) {
        CategoryAdapter.clickListener = clickListener;
    }
}