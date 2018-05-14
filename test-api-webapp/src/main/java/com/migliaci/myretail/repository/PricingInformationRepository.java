package com.migliaci.myretail.repository;

import com.migliaci.myretail.response.PricingInformation;

import javax.ws.rs.WebApplicationException;


/**
 * Interface containing method signatures for read/write
 * operations to cache/DB.
 *
 * @Author migliaci
 */
public interface PricingInformationRepository {

     PricingInformation readPricingInformation(String id);

     boolean writePricingInformation(PricingInformation pricingInformation);
}
