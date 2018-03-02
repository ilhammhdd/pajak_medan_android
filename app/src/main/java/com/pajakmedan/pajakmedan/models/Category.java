package com.pajakmedan.pajakmedan.models;

public class Category {

    public int categoryId;
    public String categoryName;
    public String categoryImageUrl;

    public Category(int categoryId, String categoryImageUrl, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
    }
}
