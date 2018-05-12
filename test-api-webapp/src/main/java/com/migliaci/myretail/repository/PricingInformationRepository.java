package com.migliaci.myretail.repository;

import com.migliaci.myretail.response.PricingInformation;

import javax.ws.rs.WebApplicationException;

public interface PricingInformationRepository {

     PricingInformation readPricingInformation(String id);

     boolean writePricingInformation(PricingInformation pricingInformation);
}
