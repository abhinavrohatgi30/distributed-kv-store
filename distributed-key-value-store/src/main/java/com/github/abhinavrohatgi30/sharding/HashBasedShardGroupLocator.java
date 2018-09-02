package com.github.abhinavrohatgi30.sharding;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.cluster.model.ShardGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class HashBasedShardGroupLocator implements ShardGroupLocator {

    @Autowired
    private ClusterConfig clusterConfig;

    private NavigableMap<Integer,String> shardGroupLookupMap;

    public HashBasedShardGroupLocator(){
        shardGroupLookupMap =  new TreeMap<>();
        for(Map.Entry<String,ShardGroup> entry : clusterConfig.getShardGroups().entrySet()){
            shardGroupLookupMap.put(entry.getValue().getHashFloor(),entry.getKey());
        }
    }

    public String locate(String key){
        int hashCode = key.hashCode();
        return shardGroupLookupMap.floorEntry(hashCode).getValue();
    }
}
