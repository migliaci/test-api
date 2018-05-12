package com.migliaci.myretail.resource

import com.migliaci.myretail.service.ProductService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
        ResultActions response = mvc.perform(get("/myretail/v1/products/13860428"))

        then:
        response.andExpect(status().is2xxSuccessful())
    }

    def 'Error path - id provided for GET endpoint is invalid' () {
        when:
        ResultActions response = mvc.perform(get("/myretail/v1/products/foobar"))

        then:
        NestedServletException nse = thrown(NestedServletException)

        and:
        nse.getCause().getMessage().contains("invalid id")
    }


    //TODO: create unit tests for bonus PUT endpoint.

}
