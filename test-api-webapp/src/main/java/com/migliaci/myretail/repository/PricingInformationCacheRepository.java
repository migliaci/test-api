package com.migliaci.myretail.repository;

import com.migliaci.myretail.response.PricingInformation;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.ws.rs.WebApplicationException;

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
        //cache will map id -> price information

        //first check the cache
        Element result = priceCache.get(id);
        if(result == null) {
            System.out.println("Cache miss! Going to DB.");
            PricingInformation pricingInformation = dbRepository.readPricingInformation(id);
            priceCache.put(new Element(id, pricingInformation));
            return pricingInformation;
        } else {
            System.out.println("Cache hit! Returning item from cache.");
            return (PricingInformation) result.getObjectValue();
        }
    }

    public boolean writePricingInformation(PricingInformation pricingInformation) {
        //Making a conscious choice here to allow users to add pricing information for items
        //that don't exist yet in datastore.
        //To avoid unnecessary caching, we won't add this to the cache until it's queried via GET.
        return dbRepository.writePricingInformation(pricingInformation);
    }


}
