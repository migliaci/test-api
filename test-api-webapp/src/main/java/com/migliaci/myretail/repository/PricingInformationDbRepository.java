package com.migliaci.myretail.repository;

import com.migliaci.myretail.dao.PricingInformationDao;
import com.migliaci.myretail.exception.MissingDataException;
import com.migliaci.myretail.response.PricingInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.ws.rs.WebApplicationException;

/**
 * Repository for managing read/write to database.
 * It will throw a MissingDataException if information
 * could not be retrieved.
 *
 * @Author migliaci
 */
@Repository
public class PricingInformationDbRepository implements PricingInformationRepository {

    @Autowired
    private PricingInformationDao dao;

    public PricingInformation readPricingInformation(String id) {

        //attempt read
        PricingInformation result = dao.readPricingInformation(id);
        if (result == null) {
            throw new MissingDataException("Pricing information for ID=" + id + " not found in datastore");
        } else {
            return result;
        }
    }

    public boolean writePricingInformation(PricingInformation pricingInformation) {
        //Making a conscious choice here to allow users to add pricing information for items
        //that don't exist yet in our datastore.
        return dao.writePricingInformation(pricingInformation);
    }
}
