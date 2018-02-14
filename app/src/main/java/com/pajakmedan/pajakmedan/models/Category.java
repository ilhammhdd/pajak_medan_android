package com.pajakmedan.pajakmedan.models;

public class Category {

    public String categoryName;
    public String categoryImageUrl;

    public Category(String categoryImageUrl, String categoryName) {
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
    }
}
