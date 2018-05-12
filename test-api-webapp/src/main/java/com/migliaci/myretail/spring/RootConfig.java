package com.migliaci.myretail.spring;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.management.ManagementService;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.cassandraunit.dataset.CQLDataSet;
import org.cassandraunit.dataset.cql.FileCQLDataSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;

import javax.management.MBeanServer;
import java.io.File;
import java.lang.management.ManagementFactory;


/**
 * Responsible for initializing core set of beans and declaring
 * property sources for application
 *
 * @Author migliaci
 */
@Configuration
@ComponentScan(basePackages = {
        "com.migliaci.myretail.resource",
        "com.migliaci.myretail.service",
        "com.migliaci.myretail.repository",
        "com.migliaci.myretail.client",
        "com.migliaci.myretail.spring",
        "com.migliaci.myretail.dao",
        "com.migliaci.myretail.util",
        "com.migliaci.myretail.interceptors"
})
@Import({CassandraConfig.class, MvcConfig.class})
@PropertySources({
        @PropertySource("classpath:/config/default.properties"),
        @PropertySource("classpath:/config/environments/${environment:local}.properties"),
})
public class RootConfig {

    @Value("${ehcache.max.items.per.cache}")
    private Integer maxNumberOfItemsPerCache;

    @Value("${ehcache.time.to.live.seconds}")
    private Integer timeToLiveSeconds;

    @Value("${product.price.cache.name}")
    private String productPriceCacheName;

    @Value("${myretail.cache.manager.name}")
    private String myRetailCacheManagerName;


    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CacheManager cacheManager() {

        MemoryStoreEvictionPolicy evictionPolicy = MemoryStoreEvictionPolicy.LRU;

        Cache priceCache = new Cache(new CacheConfiguration(productPriceCacheName, maxNumberOfItemsPerCache)
                .eternal(false)
                .memoryStoreEvictionPolicy(evictionPolicy)
                .timeToLiveSeconds(timeToLiveSeconds)
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE)));

        net.sf.ehcache.config.Configuration managerConfiguration = new net.sf.ehcache.config.Configuration();
        managerConfiguration.setName(myRetailCacheManagerName);
        CacheManager manager = CacheManager.create(managerConfiguration);
        manager.addCache(priceCache);
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ManagementService.registerMBeans(manager, mBeanServer, false, true, true, true);
        return manager;
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        //should be flexible to changes in external API calls, not fail if we find a field we don't recognize.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
