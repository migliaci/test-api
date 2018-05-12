package com.migliaci.myretail

import com.datastax.driver.core.Session
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.CQLDataSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.jndi.SimpleNamingContextBuilder
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes=[TestCassandraConfig])
class SimpleCassandraSpec extends Specification {

    @Autowired
    Session session

    @Autowired
    CQLDataSet cassandraDataSet

    void setupSpec() {

        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder()
    }

    def setup() {

        CQLDataLoader cqlDataLoader = new CQLDataLoader(session)
        cqlDataLoader.load(cassandraDataSet)
    }
}
