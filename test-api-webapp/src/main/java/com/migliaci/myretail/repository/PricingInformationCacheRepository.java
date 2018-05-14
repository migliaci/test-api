package com.migliaci.myretail.repository;

import com.migliaci.myretail.response.PricingInformation;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.ws.rs.WebApplicationException;

/**
 * Repository for managing the cache. We will add items
 * to the cache if and only if we have successfully retrieved
 * them from the database.
 *
 * @Author migliaci
 */
@Repository
public class PricingInformationCacheRepository implements PricingInformationRepository {

    @Autowired
    PricingInformationDbRepository dbRepository; //delegate when cache miss.

    @Autowired
    CacheManager cacheManager;

    Cache priceCache = null;

    @PostConstruct
    public void setupCache() {
        priceCache = cacheManager.getCache("priceCache");
    }

    public PricingInformation readPricingInformation(String id) {

        //cache will be mapped by id.
        //Determine whether item exists in the cache. If item is not found, attempt to retrieve from database.
        Element result = priceCache.get(id);
        if(result == null) {
            //cache miss - go to DB
            PricingInformation pricingInformation = dbRepository.readPricingInformation(id);
            priceCache.put(new Element(id, pricingInformation));
            return pricingInformation;
        } else {
            //cache hit - return cached value
            return (PricingInformation) result.getObjectValue();
        }
    }

    public boolean writePricingInformation(PricingInformation pricingInformation) {
        //Making a conscious choice here to allow users to add pricing information for items
        //that are not found when calling myRetail via restClient.
        //Reasoning: what if myRetail is updated to add those ids?
        //However, need to expire the value in cache and add new value if this is added via update.
        boolean result = false;

        if(priceCache.get(pricingInformation.getId()) != null){
            priceCache.remove(pricingInformation.getId());
        }

        result = dbRepository.writePricingInformation(pricingInformation);

        if(result) {
            //update cache with new value
            priceCache.put(new Element(pricingInformation.getId(), pricingInformation));
        }

        return result;
    }
}
