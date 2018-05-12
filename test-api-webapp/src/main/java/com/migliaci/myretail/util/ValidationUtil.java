package com.migliaci.myretail.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;


/**
 * Represents a set of basic utilities that can be used for request field and
 * input validation.
 *
 * @Author migliaci
 */
public class ValidationUtil {

    public static void validateNotNull(Object value, final String name) {

        if (value == null) {
            throw new IllegalArgumentException("invalid " + name + ": [null]");
        }
    }

    public static void validateNotBlank(String value, final String name) {

        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("invalid " + name + ": [" + value + "] (must not be blank)");
        }
    }

    public static void validateNumeric(String value, final String name) {

        if (!StringUtils.isNumeric(value)) {
            throw new IllegalArgumentException("invalid " + name + ": [" + value + "] (must be numeric)");
        }
    }

    public static void validatePositive(Number value, final String name) {

        validateNotNull(value, name);
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException("invalid " + name + ": [" + value + "] (must be positive)");
        }
    }

    public static BigInteger validateAndReturnPositiveBigInteger(String value, final String name) {

        validatePositiveBigInteger(value, name);
        return new BigInteger(value.trim());
    }

    public static void validatePositiveBigInteger(String value, final String name) {

        validateNotBlank(value, name);
        try {
            validatePositive(new BigInteger(value.trim()), name);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid " + name + ": [" + value + "] (must be a valid big integer value)");
        }

    }

    public static void validatePositiveDouble(String value, final String name) {

        validateNotBlank(value, name);
        try {
            validatePositive(new Double(value.trim()), name);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid " + name + ": [" + value + "] (must be a valid double value)");
        }

    }


    public static Double validateAndReturnPositiveDouble(String value, final String name) {
        validatePositiveDouble(value, name);
        return new Double(value.trim());
    }


}
