package com.timotiusoktorio.pencake.data.model;

@SuppressWarnings("unused")
public class Category {

    private String id;
    private String name;
    private Integer index;

    public Category() {
    }

    public Category(String id, String name, Integer index) {
        this.id = id;
        this.name = name;
        this.index = index;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}