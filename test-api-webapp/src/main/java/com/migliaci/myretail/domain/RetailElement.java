package com.migliaci.myretail.domain;

/**
 * POJO representing top-layer element returned from myRetail API.
 *
 * @Author migliaci
 */
public class RetailElement {

    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
