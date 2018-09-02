package com.github.abhinavrohatgi30.routing;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestRouterFactory {

    @Autowired
    private static ClusterConfig clusterConfig;

    public static RequestRouter getRequestRouter(String shardGroup){
        if(shardGroup.equals(clusterConfig.getMyGroup()))
            return new ReplicaRequestRouter();
        else
            return new ShardRequestRouter();
    }
}
