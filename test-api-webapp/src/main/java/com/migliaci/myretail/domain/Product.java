package com.migliaci.myretail.domain;

public class Product {
    //product contains item (and some other stuff)
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
