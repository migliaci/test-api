package com.migliaci.myretail.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migliaci.myretail.response.PricingInformation;
import com.migliaci.myretail.util.ValidationUtil;


/**
 * Resource containing logic for GET and PUT methods for pricing information
 * from myRetail services. Request-level validation is performed first.
 *
 * @Author migliaci
 */
public class ProductRequest {

    String name;
    @JsonProperty("current_price")
    PricingInformation currentPrice;

    public ProductRequest() {
    }

    public ProductRequest(String name, PricingInformation currentPrice) {

        ValidationUtil.validateNotBlank(name, "name");
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PricingInformation getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(PricingInformation currentPrice) {
        this.currentPrice = currentPrice;
    }

}
