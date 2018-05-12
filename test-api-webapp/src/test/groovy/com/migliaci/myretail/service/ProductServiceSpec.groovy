package com.migliaci.myretail.service

import com.migliaci.myretail.client.MyRetailRestClient
import com.migliaci.myretail.domain.CurrencyCode
import com.migliaci.myretail.domain.Item
import com.migliaci.myretail.domain.ProductDescription
import com.migliaci.myretail.exception.MissingDataException
import com.migliaci.myretail.repository.PricingInformationCacheRepository
import com.migliaci.myretail.response.PricingInformation
import com.migliaci.myretail.response.ProductResponse
import spock.lang.Specification

class ProductServiceSpec extends Specification{

    ProductService service = new ProductService()

    MyRetailRestClient mockMyRetailRestClient = Mock(MyRetailRestClient)
    PricingInformationCacheRepository mockCacheRepository = Mock(PricingInformationCacheRepository)
    Item validItem = new Item()

    PricingInformation validPricingInformation = new PricingInformation()

    void setup() {
        service.pricingInformationRepository = mockCacheRepository
        service.myRetailRestClient = mockMyRetailRestClient
        validItem.id = "12345"
        validItem.productDescription = new ProductDescription(title: "The Greatest and Best Movie In The World", description: "Self-explanatory")
        validPricingInformation.currencyCode = CurrencyCode.USD
        validPricingInformation.id = validItem.id
        validPricingInformation.value = 42.42
    }

    def "Happy path - both myRetailRestClient and pricingInformationRepository return values"() {

        setup:
        BigInteger integerId = new BigInteger(validItem.id)

        when:
        ProductResponse response = service.getProductNameAndPrice(integerId)

        then:
        1 * mockMyRetailRestClient.getItemFromExternalApi(integerId) >> validItem
        1 * mockCacheRepository.readPricingInformation(validItem.id) >> validPricingInformation
        response.id == integerId
        response.currentPrice.value == validPricingInformation.value

    }

    def "Error path -  myRetailRestClient could not find a matching value"() {

        setup:
        BigInteger integerId = new BigInteger(0)

        when:
        ProductResponse response = service.getProductNameAndPrice(integerId)

        then:
        1 * mockMyRetailRestClient.getItemFromExternalApi(integerId)
        0 * mockCacheRepository.readPricingInformation(validItem.id)

        and:
        MissingDataException mde = thrown(MissingDataException)
        mde.getMessage().contains("Product name retrieval was unsuccessful")

    }

    def "Error path -  We could not find a price value for the provided id in our cache or datastore"() {

        setup:
        BigInteger integerId = new BigInteger(validItem.id)

        when:
        ProductResponse response = service.getProductNameAndPrice(integerId)

        then:
        1 * mockMyRetailRestClient.getItemFromExternalApi(integerId) >> validItem
        1 * mockCacheRepository.readPricingInformation(validItem.id) >> null

        and:
        MissingDataException mde = thrown(MissingDataException)
        mde.getMessage().contains("Product price retrieval was unsuccessful")

    }



}
