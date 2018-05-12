package com.migliaci.myretail.domain;

public enum CurrencyCode {
    USD; //US Dollar

    //more options can be added as needed.
    //TODO: modify db so more than one ID can exist for different currencies. Out of scope for POC.

    public static CurrencyCode fromString(String potentialCode) {
        try {
            return CurrencyCode.valueOf(potentialCode.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid currency code: " + potentialCode + " (no such code)");
        }
    }

}
