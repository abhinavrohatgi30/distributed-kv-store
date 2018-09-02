package com.github.abhinavrohatgi30.routing;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.service.FileKeyValueStoreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestRouterFactory {

    private static final Logger logger = LogManager.getLogger(FileKeyValueStoreService.class);


    public static RequestRouter getRequestRouter(String shardGroup,ClusterConfig clusterConfig){
        if(shardGroup.equals(clusterConfig.getMyGroup()))
            return new ReplicaRequestRouter(clusterConfig);
        else
            return new ShardRequestRouter(clusterConfig);
    }
}
