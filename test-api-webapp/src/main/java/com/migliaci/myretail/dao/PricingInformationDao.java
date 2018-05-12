package com.migliaci.myretail.dao;

import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migliaci.myretail.domain.CurrencyCode;
import com.migliaci.myretail.exception.DataAccessException;
import com.migliaci.myretail.response.PricingInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;

@Component
public class PricingInformationDao {

    @Autowired
    Session session;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${cassandra.keyspace}")
    String keyspace;

    PreparedStatement writePriceInformation;
    PreparedStatement readPriceInformation;

    @PostConstruct
    public void setupPreparedStatements() {
        try {
            writePriceInformation = session.prepare("INSERT INTO " + keyspace + ".pricing_metadata (id, value, currency_code, updt_ts) VALUES (?, ?, ?, ?)");
            readPriceInformation = session.prepare("SELECT ID, VALUE, CURRENCY_CODE, UPDT_TS FROM " + keyspace + ".pricing_metadata WHERE ID = ?");
        } catch (InvalidQueryException iqe) {
            System.out.println("Error! Keyspace does not exist. Data will need to be generated before API will return valid results.");
        }

    }

    public PricingInformation readPricingInformation(String id) {

        PricingInformation finalResult = null;

        try {
            BoundStatement readStatement = readPriceInformation.bind(id);
            ResultSet rs = session.execute(readStatement);
            if (rs.getAvailableWithoutFetching() == 0) {
                return null;
            } else {
                Row row = rs.one();
                if(row != null) {
                    finalResult = new PricingInformation(
                       row.getString("ID"),
                       row.getDouble("VALUE"),
                       CurrencyCode.fromString(row.getString("CURRENCY_CODE")),
                       row.getTimestamp("UPDT_TS").toInstant()
                    );

                }
            }

        } catch (Exception e) {
            throw new DataAccessException("readPricingInformation failed", e);
        }
        return finalResult;
    }

    public boolean writePricingInformation(PricingInformation pricingInformation) {
        boolean result = false;

        try {
            Date updateTimestamp = new Date();
            BoundStatement writeStatement = writePriceInformation.bind();
            writeStatement.setString("ID", pricingInformation.getId());
            writeStatement.setDouble("VALUE", pricingInformation.getValue());
            writeStatement.setString("CURRENCY_CODE", pricingInformation.getCurrencyCode().name());
            writeStatement.setTimestamp("UPDT_TS", updateTimestamp);
            ResultSet rs = session.execute(writeStatement);
            if (rs != null) {
                result = rs.wasApplied();
            }
        } catch (Exception e) {
            throw new DataAccessException("writePricingInformation failed", e);
        }
        return result;
    }

}
