package com.migliaci.myretail.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.migliaci.myretail.domain.CurrencyCode
import com.migliaci.myretail.request.ProductRequest
import com.migliaci.myretail.response.PricingInformation
import com.migliaci.myretail.service.ProductService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import spock.lang.Specification

import javax.ws.rs.core.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ProductResourceSpec extends Specification {

    private MockMvc mvc
    private ProductService mockService = Mock(ProductService)
    private ProductResource resource = new ProductResource()

    void setup() {
        resource.productApiService = mockService
        mvc = MockMvcBuilders.standaloneSetup(resource).build()
    }

    def 'Happy path - id for GET endpoint provided is a numeric value' () {
        when:
        ResultActions response = mvc.perform(get("/myretail/v1/products/{id}", "123346"))

        then:
        response.andExpect(status().is2xxSuccessful())
    }

    def 'Error path - id provided for GET endpoint is invalid' () {
        when:
        ResultActions response = mvc.perform(get("/myretail/v1/products/{id}", "foobar"))

        then:
        NestedServletException nse = thrown(NestedServletException)

        and:
        nse.getCause().getMessage().contains("invalid id")
    }

    def 'Happy path - request for PUT endpoint provided is valid' () {
        setup:
        ProductRequest request = new ProductRequest("productName", new PricingInformation(42.00, CurrencyCode.USD))
        String requestAsString = createPutRequestInJson(request)

        when:
        ResultActions response = mvc.perform(put("/myretail/v1/products/{id}", "13860428")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestAsString))

        then:
        1 * mockService.updateProductPrice(_ as String, _ as ProductRequest) >> true
        response.andExpect(status().is2xxSuccessful())
    }

    def 'Error path - request for PUT endpoint provided did not complete successfully' () {
        setup:
        ProductRequest request = new ProductRequest("productName", new PricingInformation(42.00, CurrencyCode.USD))
        String requestAsString = createPutRequestInJson(request)

        when:
        ResultActions response = mvc.perform(put("/myretail/v1/products/{id}", "13860428")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestAsString))

        then:
        1 * mockService.updateProductPrice(_ as String, _ as ProductRequest) >> false
        response.andExpect(status().is4xxClientError())
    }

    def 'Error path - request for PUT endpoint provided sent a bad id value' () {
        setup:
        ProductRequest request = new ProductRequest("productName", new PricingInformation(42.00, CurrencyCode.USD))
        String requestAsString = createPutRequestInJson(request)

        when:
        ResultActions response = mvc.perform(put("/myretail/v1/products/{id}", "foo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestAsString))

        then:
        NestedServletException nse = thrown(NestedServletException)

        and:
        nse.getCause().getMessage().contains("invalid id")
    }

    private static String createPutRequestInJson (ProductRequest request) {
        ObjectMapper objectMapper = new ObjectMapper()
        return objectMapper.writeValueAsString(request)
    }

}
