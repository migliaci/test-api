package com.migliaci.myretail.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.migliaci.myretail.SimpleCassandraSpec
import com.migliaci.myretail.domain.CurrencyCode
import com.migliaci.myretail.response.PricingInformation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes=[PricingInformationDao, PricingInformationDaoTestConfig])
class PricingInformationDaoIntegrationSpec extends SimpleCassandraSpec {

    @Autowired
    PricingInformationDao dao

    def 'test get existing data'() {

        setup:
        String id = "13860428"

        when: 'read the information from the DAO'
        PricingInformation pricingInformation = dao.readPricingInformation(id)

        then:
        pricingInformation.id == "13860428"
        pricingInformation.currencyCode == CurrencyCode.USD
        pricingInformation.value == 29.99d

    }

    def 'test post new data'() {

        setup:
        String id = "13860428"
        //delete record from database so we can reinsert.
        session.execute("DELETE FROM myretail.pricing_metadata WHERE ID= '${id}'")
        PricingInformation pricingInformation = new PricingInformation("13860428", 49.99d, CurrencyCode.USD, null)


        when: 'we add the object again'
        boolean result = dao.writePricingInformation(pricingInformation)
        PricingInformation writtenInformation = dao.readPricingInformation(id)

        then:
        result
        writtenInformation.id == id
        writtenInformation.value == 49.99d
        writtenInformation.currencyCode == CurrencyCode.USD

    }

    def 'test put existing data'() {

        setup:
        String id = "13860428"
        PricingInformation pricingInformation = new PricingInformation("13860428", 99.99d, CurrencyCode.USD, null)


        when: 'we add the object again'
        boolean result = dao.writePricingInformation(pricingInformation)
        PricingInformation writtenInformation = dao.readPricingInformation(id)

        then:
        result
        writtenInformation.id == id
        writtenInformation.value == 99.99d
        writtenInformation.currencyCode == CurrencyCode.USD

    }

    @Configuration
    @PropertySources([
        @PropertySource('classpath:/config/default.properties'),
        @PropertySource('classpath:/config/environments/${environment:local}.properties')
    ])
    private static class PricingInformationDaoTestConfig {

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper()
        }
    }
}


