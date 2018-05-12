package com.migliaci.myretail

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.core.QueryOptions
import com.datastax.driver.core.Session
import net.sf.ehcache.Cache
import net.sf.ehcache.CacheManager
import net.sf.ehcache.config.CacheConfiguration
import net.sf.ehcache.config.PersistenceConfiguration
import net.sf.ehcache.management.ManagementService
import net.sf.ehcache.store.MemoryStoreEvictionPolicy
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.CQLDataSet
import org.cassandraunit.dataset.cql.FileCQLDataSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

import javax.management.MBeanServer
import java.lang.management.ManagementFactory

@Configuration
class TestCassandraConfig {

    //TODO: move these into configuration files
    @Value('myretail')
    private String keyspace

    @Value('192.168.1.136')
    private List<String> contactPoints

    @Value('9042')
    private int port

    @Value('/data/cql')
    private String cqlDirectory

    @Value('1000')
    private Integer maxNumberOfItemsPerCache

    @Value('30')
    private Integer timeToLiveSeconds

    @Value('priceCache')
    private String productPriceCacheName

    @Value('myRetailCacheManager')
    private String myRetailCacheManagerName

    @Bean
    CQLDataSet cassandraDataSetDev() {
        //get the file
        File root = new File(this.class.getResource(cqlDirectory).file)
        File[] files = root.listFiles()

        if(files != null && files.length > 0) {
            File cqlToLoad = files[0]
            return new FileCQLDataSet(cqlToLoad.absolutePath, true, true, keyspace)

        } else {
            System.out.println("Error. CQL file misconfigured.")
            throw new IllegalArgumentException("CQL File Misconfiguration")
        }
    }

    @Bean
    Cluster cluster() {
        Cluster.Builder builder = Cluster.builder()
        //InetSocketAddress socketAddress = new InetSocketAddress(contactPoints.get(0), port)
        //builder.addContactPointsWithPorts()
        builder.addContactPointsWithPorts(contactPoints.collect {new InetSocketAddress(it, 9042)})
        builder.withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_ONE))

        return builder.build()
    }

    @Bean
    Session session(Cluster cluster, CQLDataSet dataSet) throws Exception {

        Session s = cluster.connect()
        CQLDataLoader cqlDataLoader = new CQLDataLoader(s)
        cqlDataLoader.load(dataSet)
        return s
    }

    @Bean
    public CacheManager cacheManager() {

        CacheManager manager

        manager = CacheManager.getCacheManager(myRetailCacheManagerName)
        if(manager != null) {
            return manager
        }

        MemoryStoreEvictionPolicy evictionPolicy = MemoryStoreEvictionPolicy.LRU;

        Cache priceCache = new Cache(new CacheConfiguration(productPriceCacheName, maxNumberOfItemsPerCache)
                .eternal(false)
                .memoryStoreEvictionPolicy(evictionPolicy)
                .timeToLiveSeconds(timeToLiveSeconds)
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE)))

        net.sf.ehcache.config.Configuration managerConfiguration = new net.sf.ehcache.config.Configuration()
        managerConfiguration.setName(myRetailCacheManagerName)
        manager = CacheManager.create(managerConfiguration)
        manager.addCacheIfAbsent(priceCache)
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer()
        ManagementService.registerMBeans(manager, mBeanServer, false, true, true, true)
        return manager
    }

}
