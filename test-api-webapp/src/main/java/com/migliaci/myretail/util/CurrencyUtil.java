package com.migliaci.myretail.util;

import com.migliaci.myretail.domain.CurrencyCode;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtil {

    public static Double transformPrice(Double value, CurrencyCode currencyCode) {

        //TODO: logic around additional currencies can be implemented here.
        if(currencyCode == CurrencyCode.USD) {
            return roundTwoDecimalPlaces(value);
        }
        return value;
    }

    private static Double roundTwoDecimalPlaces(Double val) {
        return new BigDecimal(val.toString()).setScale(2,RoundingMode.HALF_UP).doubleValue();
    }
}
