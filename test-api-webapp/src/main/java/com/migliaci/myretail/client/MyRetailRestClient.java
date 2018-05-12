package com.migliaci.myretail.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migliaci.myretail.domain.Item;
import com.migliaci.myretail.domain.Product;
import com.migliaci.myretail.domain.RetailElement;
import com.migliaci.myretail.exception.MissingDataException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Rest client to retrieve name information from myRetail.
 *
 * @Author migliaci
 */
@Component
public class MyRetailRestClient {

    @Value("${myretail.base.url}") //configurable URL
    private String baseUrl;

    @Value("${myretail.exclusions}") //configurable options
    private String exclusions;

    @Autowired
    private ObjectMapper objectMapper;

    public Item getItemFromExternalApi(BigInteger id) {

            Client client = Client.create();

            WebResource webResource = client.resource(buildUrl(id));

            //execute request
            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            //if myRetail returns 404, 400, etc, we will document this to the caller.
            if (response.getStatus() != 200) {
                throw new HttpClientErrorException(HttpStatus.valueOf(response.getStatus()), ": Non-200 status code returned from external resource");
            }

            String output = response.getEntity(String.class);
            Product product = null;
            try {
                product = objectMapper.readValue(output, RetailElement.class).getProduct();
            } catch (IOException e) {
                throw new IllegalArgumentException("Retail element parsing failed", e);
            }

            return product.getItem();
    }

    String buildUrl(BigInteger id) {
        StringBuilder finalUrl = new StringBuilder();
        if(StringUtils.isBlank(baseUrl)) {
            //misconfig - this shouldn't happen - exit
            throw new MissingDataException("baseUrl is not present in configuration");
        }

        if(StringUtils.isBlank(exclusions)) {
            //no exclusions present.
            return finalUrl.append(baseUrl)
                    .append(id.toString()).toString();
        } else {
            //exclusions present, so build from there
            return finalUrl.append(baseUrl)
                    .append(id.toString())
                    .append("?excludes=")
                    .append(exclusions).toString();
        }

    }


}
