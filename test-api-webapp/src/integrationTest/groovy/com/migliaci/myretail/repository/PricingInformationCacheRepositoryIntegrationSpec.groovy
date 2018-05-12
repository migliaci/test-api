package com.migliaci.myretail.repository

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migliaci.myretail.SimpleCassandraSpec
import com.migliaci.myretail.dao.PricingInformationDao
import com.migliaci.myretail.domain.CurrencyCode
import com.migliaci.myretail.response.PricingInformation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes=[PricingInformationDao, PricingInformationDbRepository, PricingInformationCacheRepository, PricingInformationCacheRepositoryTestConfig])
class PricingInformationCacheRepositoryIntegrationSpec extends SimpleCassandraSpec {

    @Autowired
    PricingInformationDao dao

    @Autowired
    PricingInformationCacheRepository repository

    def 'test data is added to cache when necessary'() {

        setup:
        String id = "13860428"

        when: 'read the information from the cache'
        PricingInformation pricingInformation = repository.readPricingInformation(id)
        PricingInformation cachedPricingInformation = repository.readPricingInformation(id)

        then:
        repository.priceCache.size == 1
        pricingInformation == cachedPricingInformation
        pricingInformation.id == "13860428"
        pricingInformation.currencyCode == CurrencyCode.USD
        pricingInformation.value == 29.99d

    }



    @Configuration
    @PropertySources([
            @PropertySource('classpath:/config/default.properties'),
            @PropertySource('classpath:/config/environments/${environment:local}.properties')
    ])
    private static class PricingInformationCacheRepositoryTestConfig {

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper()
        }
    }
}

