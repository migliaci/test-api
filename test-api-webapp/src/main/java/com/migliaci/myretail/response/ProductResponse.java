package com.migliaci.myretail.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.migliaci.myretail.request.ProductRequest;

import java.math.BigInteger;

/**
 * Response containing both name and price information,
 * as well as an integer id.
 *
 * @Author migliaci
 */
@JsonPropertyOrder({"id", "name", "current_price"})
public class ProductResponse extends ProductRequest {

    BigInteger id;

    public ProductResponse() {
    }

    public ProductResponse(BigInteger id, String name, PricingInformation currentPrice) {
        super(name, currentPrice);
        this.id = id;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }
}
