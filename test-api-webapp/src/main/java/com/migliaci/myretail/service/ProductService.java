package com.migliaci.myretail.service;

import com.migliaci.myretail.client.MyRetailRestClient;
import com.migliaci.myretail.domain.Item;
import com.migliaci.myretail.exception.MissingDataException;
import com.migliaci.myretail.repository.PricingInformationCacheRepository;
import com.migliaci.myretail.request.ProductRequest;
import com.migliaci.myretail.response.PricingInformation;
import com.migliaci.myretail.response.ProductResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * Service that contains logic for merging the responses
 * from myRetail external API and internal pricing database
 *
 * @Author migliaci
 */
@Service
public class ProductService {

    @Autowired
    MyRetailRestClient myRetailRestClient;

    @Autowired
    PricingInformationCacheRepository pricingInformationRepository;

    public ProductResponse getProductNameAndPrice(BigInteger id) {

        //first get item element from myRetail via restClient.
        //If that fails, return 404 not found.  We can't return a proper response without a product name.
        Item item = myRetailRestClient.getItemFromExternalApi(id);
        if (item == null || StringUtils.isEmpty(item.getId()) || item.getProductDescription() == null
                || item.getProductDescription().getTitle() == null) {
            //didn't find the name, or the name was was null or empty.
            throw new MissingDataException("Product name retrieval was unsuccessful");
        } else {
            //next, retrieve pricing information from cache (or DB, if not found in cache)
            PricingInformation pricingInformation = pricingInformationRepository.readPricingInformation(item.getId());
            if (pricingInformation == null) {
                //pricing information returned from cache or DB is null
                throw new MissingDataException("Product price retrieval was unsuccessful");
            } else {
                //assemble valid response
                ProductResponse response = new ProductResponse(new BigInteger(item.getId()), item.getProductDescription().getTitle(), pricingInformation);
                return response;
            }
        }

    }

    public boolean updateProductPrice(String id, ProductRequest productRequest) {

        //update request with correct id value, which has already been validated.
        productRequest.getCurrentPrice().setId(id);
        //currently can update prices for ids that are not found via myRetail rest client.
        //choice was made because this application has no control over data retrieved from that resource.
        return pricingInformationRepository.writePricingInformation(productRequest.getCurrentPrice());
    }

}
