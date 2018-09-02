package com.github.abhinavrohatgi30.service;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.dao.DAO;
import com.github.abhinavrohatgi30.model.RequestPayload;
import com.github.abhinavrohatgi30.routing.ReplicaRequestRouter;
import com.github.abhinavrohatgi30.routing.RequestRouter;
import com.github.abhinavrohatgi30.routing.RequestRouterFactory;
import com.github.abhinavrohatgi30.sharding.ShardGroupLocator;
import com.github.abhinavrohatgi30.util.ResponseMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FileKeyValueStoreService implements  KeyValueStoreService {

    private DAO dao;

    private ShardGroupLocator shardGroupLocator;

    private ClusterConfig clusterConfig;

    private static final Logger logger = LogManager.getLogger(FileKeyValueStoreService.class);

    public FileKeyValueStoreService(@Autowired ClusterConfig clusterConfig, @Autowired @Qualifier("fileDAO") DAO dao, @Autowired ShardGroupLocator shardGroupLocator){
        this.dao = dao;
        this.clusterConfig = clusterConfig;
        this.shardGroupLocator = shardGroupLocator;
    }

    @Override
    public String getValue(String key) {
        String shardGroup = shardGroupLocator.locate(key);
        RequestPayload requestPayload = new RequestPayload(key);
        requestPayload.setShardGroup(shardGroup);
        RequestRouter requestRouter = RequestRouterFactory.getRequestRouter(shardGroup,clusterConfig);
        logger.debug(String.format("Request Router for the key %s is %s",key,requestRouter.toString()));
        if(requestRouter instanceof ReplicaRequestRouter) {
            try {
                return dao.read(key);
            }catch (NullPointerException e){
                return ResponseMessage.KEY_NOT_AVAILABLE;
            }
        }
        else {
                return requestRouter.routeGetRequest(requestPayload);
        }
    }

    @Override
    public String indexKeyValue(String key, String value,Boolean isRouted) {
        String shardGroup = shardGroupLocator.locate(key);
        RequestPayload requestPayload = new RequestPayload(key,value);
        requestPayload.setShardGroup(shardGroup);
        RequestRouter requestRouter = RequestRouterFactory.getRequestRouter(shardGroup,clusterConfig);
        logger.debug(String.format("Request Router for the key %s is %s and the request isRouted: %b",key,requestRouter.toString(),isRouted));
        if (requestRouter instanceof  ReplicaRequestRouter){
            try {
                dao.write(key, value);
            }catch (Exception e){
                return ResponseMessage.WRITE_OPERATION_FAILED;
            }
            if(!isRouted){
                return requestRouter.routePutRequest(requestPayload);
            }
            return  ResponseMessage.WRITE_OPERATION_SUCCESS;
        }else {
            return requestRouter.routePutRequest(requestPayload);
        }
    }
}
