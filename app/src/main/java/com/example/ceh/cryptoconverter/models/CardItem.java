package com.example.ceh.cryptoconverter.models;

import java.io.Serializable;

/**
 * Created by CEH on 11/4/2017.
 */

public class CardItem implements Serializable {
    String currency_name;
    String currency_symbol;
    double btc_value;
    double eth_value;
    int currency_img_drawable;

    public CardItem() {
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public double getBtc_value() {
        return btc_value;
    }

    public void setBtc_value(double btc_value) {
        this.btc_value = btc_value;
    }

    public double getEth_value() {
        return eth_value;
    }

    public void setEth_value(double eth_value) {
        this.eth_value = eth_value;
    }

    public int getCurrency_img_drawable() {
        return currency_img_drawable;
    }

    public void setCurrency_img_drawable(int currency_img_drawable) {
        this.currency_img_drawable = currency_img_drawable;
    }
}
