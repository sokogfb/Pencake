package com.timotiusoktorio.pencake.data.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Product {

    private String id;
    private String name;
    private String description;
    private List<Price> prices;
    private List<String> imageUrls;
    private Integer timeOfWork;
    private String timeOfWorkDesc;

    public Product() {
    }

    public Product(String id, String name, String description, List<Price> prices, List<String> imageUrls, Integer timeOfWork, String timeOfWorkDesc) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.prices = prices;
        this.imageUrls = imageUrls;
        this.timeOfWork = timeOfWork;
        this.timeOfWorkDesc = timeOfWorkDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Integer getTimeOfWork() {
        return timeOfWork;
    }

    public void setTimeOfWork(Integer timeOfWork) {
        this.timeOfWork = timeOfWork;
    }

    public String getTimeOfWorkDesc() {
        return timeOfWorkDesc;
    }

    public void setTimeOfWorkDesc(String timeOfWorkDesc) {
        this.timeOfWorkDesc = timeOfWorkDesc;
    }

    public String getCombinedPrices() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < prices.size(); i++) {
            sb.append(prices.get(i).getPriceAndSize());
            if (i != prices.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public List<String> getSizes() {
        List<String> sizes = new ArrayList<>();
        for (Price price : prices) {
            sizes.add(price.getSizeAndPrice());
        }
        return sizes;
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Product fromJson(String json) {
        return new Gson().fromJson(json, Product.class);
    }
}