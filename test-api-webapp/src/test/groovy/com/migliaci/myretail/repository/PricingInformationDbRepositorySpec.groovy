package com.migliaci.myretail.repository

import com.migliaci.myretail.dao.PricingInformationDao
import com.migliaci.myretail.domain.CurrencyCode
import com.migliaci.myretail.exception.MissingDataException
import com.migliaci.myretail.response.PricingInformation
import spock.lang.Specification

class PricingInformationDbRepositorySpec extends Specification {

    PricingInformationDbRepository repository = new PricingInformationDbRepository()
    PricingInformationDao mockDao = Mock(PricingInformationDao)

    PricingInformation validPricingInformation = new PricingInformation()

    void setup() {
        repository.dao = mockDao
        validPricingInformation.currencyCode = CurrencyCode.USD
        validPricingInformation.id = "12345"
        validPricingInformation.value = 42.42
    }

    def "Happy path - dao is called and successfully returns pricing data"() {

        setup:
        String id = validPricingInformation.id

        when:
        PricingInformation pricingInformation = repository.readPricingInformation(id)

        then:
        1 * mockDao.readPricingInformation(id) >> validPricingInformation
    }

    def "Happy path - dao is called and successfully updates pricing data"() {

        setup:
        String id = validPricingInformation.id

        when:
        boolean result = repository.writePricingInformation(validPricingInformation)

        then:
        1 * mockDao.writePricingInformation(validPricingInformation) >> true
    }

    def "Error path - dao is called but cannot return pricing data"() {

        setup:
        String id = validPricingInformation.id

        when:
        repository.readPricingInformation(id)

        then:
        1 * mockDao.readPricingInformation(id) >> null
        MissingDataException mde = thrown(MissingDataException)
    }

}
