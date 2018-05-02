package com.timotiusoktorio.pencake.data.model;

@SuppressWarnings("unused")
public class Price {

    private String size;
    private Integer price;

    public Price() {
    }

    public Price(String size, Integer price) {
        this.size = size;
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPriceAndSize() {
        return "$ " + price + " (" + size + ")";
    }

    public String getSizeAndPrice() {
        return size + " - $ " + price;
    }
}