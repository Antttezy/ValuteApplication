package com.antkumachev.valuteapplication.models;

import android.graphics.drawable.Drawable;

public class Valute {

    private String code;
    private String name;
    private float price;
    private Drawable flag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Drawable getFlag() {
        return flag;
    }

    public void setFlag(Drawable flag) {
        this.flag = flag;
    }
}
