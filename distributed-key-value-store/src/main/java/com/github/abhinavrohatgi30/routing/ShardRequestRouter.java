package com.github.abhinavrohatgi30.routing;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.cluster.model.Node;
import com.github.abhinavrohatgi30.model.RequestPayload;
import com.github.abhinavrohatgi30.util.ClientUtils;
import com.github.abhinavrohatgi30.util.ResponseMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ShardRequestRouter implements RequestRouter {


    private ClusterConfig clusterConfig;

    private static final Logger logger = LogManager.getLogger(ShardRequestRouter.class);

    public ShardRequestRouter(ClusterConfig clusterConfig){
        this.clusterConfig = clusterConfig;
    }


    @Override
    public String routePutRequest(RequestPayload requestPayload) {
        List<Node> shardGroupNodes = clusterConfig.getShardGroups().get(requestPayload.getShardGroup()).getReplicaNodes();
        String requestPath = String.format("/set/%s",requestPayload.getKey());
        for(Node node : shardGroupNodes){
            String nodeUrl = node.getNodeUrl();
            logger.debug(String.format("Routing the request to the following URL : %s",nodeUrl+requestPath));
            try {
                String response = ClientUtils.executePostRequest(nodeUrl + requestPath,requestPayload.getValue(), false);
                JSONObject responseJson = new JSONObject(response);
                return responseJson.getString("message");
            }catch (Exception e){
                e.printStackTrace();
                logger.error(String.format("Route to the following URL : %s failed", nodeUrl + requestPath));
            }
        }
        return String.format(ResponseMessage.NO_NODES_TO_HANDLE_SHARDS,requestPayload.getShardGroup());
    }

    @Override
    public String routeGetRequest(RequestPayload requestPayload) {
        List<Node> shardGroupNodes = clusterConfig.getShardGroups().get(requestPayload.getShardGroup()).getReplicaNodes();
        String requestPath = String.format("/get/%s",requestPayload.getKey());
        for(Node node : shardGroupNodes){
            String nodeUrl = node.getNodeUrl();
            logger.debug(String.format("Routing the request to the following URL : %s",nodeUrl+requestPath));
            try {
               String response =  ClientUtils.executeGetRequest(nodeUrl + requestPath, false);
               if(response != null) {
                   JSONObject responseJson = new JSONObject(response);
                   return responseJson.getString("message");
               }
               else
                   logger.error(String.format("Routing the request to the following URL : %s",nodeUrl+requestPath));
            }catch (Exception e){
                e.printStackTrace();
                logger.error(String.format("Route to the following URL : %s failed", nodeUrl + requestPath));
            }
        }
        return String.format(ResponseMessage.NO_NODES_TO_HANDLE_SHARDS,requestPayload.getShardGroup());
    }


}
