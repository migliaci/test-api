package com.migliaci.myretail.repository;

import com.migliaci.myretail.response.PricingInformation;

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
