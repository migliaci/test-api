package com.migliaci.myretail.util

import com.migliaci.myretail.domain.CurrencyCode
import spock.lang.Specification

class CurrencyUtilSpec extends Specification {

    def 'Happy path'() {

        when:
        Double result = CurrencyUtil.transformPrice(value, currencyCode)

        then:
        result == expectedResult

        where:
        value  | currencyCode     | expectedResult
        1.02   | CurrencyCode.USD | 1.02
        1.025  | CurrencyCode.USD | 1.03
        99.99  | CurrencyCode.USD | 99.99
        99.995 | CurrencyCode.USD | 100.00
        99.994 | CurrencyCode.USD | 99.99

    }
}
