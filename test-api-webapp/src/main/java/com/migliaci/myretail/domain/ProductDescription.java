package com.migliaci.myretail.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing product description element returned from myRetail API.
 *
 * @Author migliaci
 */
public class ProductDescription {

    private String title;

    @JsonProperty("general_description")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
