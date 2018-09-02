package com.github.abhinavrohatgi30.service;

import com.github.abhinavrohatgi30.dao.DAO;
import com.github.abhinavrohatgi30.model.RequestPayload;
import com.github.abhinavrohatgi30.routing.ReplicaRequestRouter;
import com.github.abhinavrohatgi30.routing.RequestRouter;
import com.github.abhinavrohatgi30.routing.RequestRouterFactory;
import com.github.abhinavrohatgi30.sharding.ShardGroupLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileKeyValueStoreService implements  KeyValueStoreService {

    @Autowired
    private DAO dao;

    @Autowired
    private ShardGroupLocator shardGroupLocator;


    @Override
    public String getValue(String key) {
        String shardGroup = shardGroupLocator.locate(key);
        RequestPayload requestPayload = new RequestPayload(key);
        RequestRouter requestRouter = RequestRouterFactory.getRequestRouter(shardGroup);
        if(requestRouter instanceof ReplicaRequestRouter)
            return dao.read(key);
        else
            return requestRouter.routeGetRequest(requestPayload);
    }

    @Override
    public int indexKeyValue(String key, String value,Boolean isRouted) {
        String shardGroup = shardGroupLocator.locate(key);
        RequestPayload requestPayload = new RequestPayload(key,value);
        RequestRouter requestRouter = RequestRouterFactory.getRequestRouter(shardGroup);
        if(requestRouter instanceof ReplicaRequestRouter && isRouted)
            return dao.write(key,value);
        else
            return requestRouter.routePutRequest(requestPayload);
    }
}
