package com.github.abhinavrohatgi30.sharding;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.cluster.model.ShardGroup;
import com.github.abhinavrohatgi30.service.FileKeyValueStoreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Configuration
public class HashBasedShardGroupLocator implements ShardGroupLocator {

    private NavigableMap<Integer,String> shardGroupLookupMap;

    private static final Logger logger = LogManager.getLogger(HashBasedShardGroupLocator.class);


    public HashBasedShardGroupLocator(@Autowired ClusterConfig clusterConfig){
        logger.debug("Initialising shard group lookup map");
        shardGroupLookupMap =  new TreeMap<>();
        for(Map.Entry<String,ShardGroup> entry : clusterConfig.getShardGroups().entrySet()){
            shardGroupLookupMap.put(entry.getValue().getHashFloor(),entry.getKey());
        }
        logger.debug("Initialised shard group lookup map");
    }

    public String locate(String key){
        int hashCode = key.hashCode();
        String shardGroup = shardGroupLookupMap.floorEntry(hashCode).getValue();
        logger.debug(String.format("Shard Group : %s found for the Key : %s",shardGroup,key) );
        return shardGroup;
    }
}
