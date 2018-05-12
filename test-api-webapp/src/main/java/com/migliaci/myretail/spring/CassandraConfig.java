package com.migliaci.myretail.spring;


import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.LoggingRetryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Responsible for initializing beans associated with Cassandra
 *
 * @Author migliaci
 */
@Configuration
public class CassandraConfig {

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Value("#{${cassandra.contactpoint}}")
    private List<String> contactPoints;

    @Value("${cassandra.port}")
    private Integer port;

    @Value("${cassandra.readTimeoutMilliseconds}")
    private Integer readTimeout;

    @Value("${cassandra.connectionRetryInMilliseconds}")
    private Long reconnect;

    @Bean
    Cluster cluster() {

        try {
            //create builder for the cluster we will be connecting to
            Cluster.Builder builder = Cluster.builder();

            //Gather up a list of InetSocketAddresses
            //created from the specified contact points and the configured port.
            builder.addContactPointsWithPorts(contactPoints.stream().map(it -> new InetSocketAddress(it, port)).collect(Collectors.toList()));

            //Optional policies for reconnection, timeouts, consistency level.
            //Not really needed for this POC, but good to call out.
            builder.withReconnectionPolicy(new ConstantReconnectionPolicy(reconnect));
            builder.withRetryPolicy(new LoggingRetryPolicy(DefaultRetryPolicy.INSTANCE));
            builder.withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_ONE));

            SocketOptions socketOptions = new SocketOptions();
            socketOptions.setReadTimeoutMillis(readTimeout);
            builder.withSocketOptions(socketOptions);
            return builder.build();
        } catch (Exception e) {

            System.out.println("An error occurred while attempting to create Cassandra cluster.");
            throw e;
        }
    }

    @Bean
    Session session(Cluster cluster) throws Exception {
        //not specifying keyspace here because we will need to do so later.
        return cluster.connect();
    }
}
