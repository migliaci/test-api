package com.migliaci.myretail.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.migliaci.myretail.domain.CurrencyCode;

import java.time.Instant;

/**
 * Class containing pricing information in response
 *
 * @Author migliaci
 */
public class PricingInformation {

    @JsonIgnore
    private String id; //won't be returned in JSON at this time - only used for lookups.

    private Double value;
    private CurrencyCode currencyCode;

    @JsonIgnore
    private Instant updateTimestamp; //won't be returned in JSON at this time - only used by database.

    public PricingInformation() {
    }

    public PricingInformation(String id, Double value, CurrencyCode currencyCode, Instant updateTimestamp) {
        this.id = id;
        this.value = value;
        this.currencyCode = currencyCode;
        this.updateTimestamp = updateTimestamp;
    }

    public PricingInformation(Double value, CurrencyCode currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}
