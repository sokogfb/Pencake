package com.timotiusoktorio.pencake.data.model;

@SuppressWarnings("unused")
public class StoreInfo {

    private String name;
    private String address;
    private String phone;
    private Coordinate coordinate;

    public StoreInfo() {
    }

    public StoreInfo(String name, String address, String phone, Coordinate coordinate) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}