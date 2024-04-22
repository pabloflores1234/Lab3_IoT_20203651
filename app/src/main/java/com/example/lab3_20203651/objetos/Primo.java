package com.example.lab3_20203651.objetos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Primo implements Serializable {

    @SerializedName("number")
    private int number;

    @SerializedName("order")
    private int order;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
