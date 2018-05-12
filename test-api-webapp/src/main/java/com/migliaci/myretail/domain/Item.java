package com.migliaci.myretail.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing item element returned from myRetail API.
 *
 * @Author migliaci
 */
public class Item {

    @JsonProperty("tcin")
    private String id;

    @JsonProperty("product_description")
    private ProductDescription productDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }
}
