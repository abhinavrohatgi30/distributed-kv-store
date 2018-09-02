package com.github.abhinavrohatgi30.routing;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.cluster.model.Node;
import com.github.abhinavrohatgi30.model.RequestPayload;
import com.github.abhinavrohatgi30.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShardRequestRouter implements RequestRouter {


    @Autowired
    private ClusterConfig clusterConfig;

    @Override
    public int routePutRequest(RequestPayload requestPayload) {
        List<Node> shardGroupNodes = clusterConfig.getShardGroups().get(requestPayload.getShardGroup()).getReplicaNodes();
        String requestPath = String.format("/set/%s",requestPayload.getKey());
        for(Node node : shardGroupNodes){
            String nodeUrl = node.getNodeUrl();
            try {
                String response = ClientUtils.executePostRequest(nodeUrl + requestPath,requestPayload.getValue(), false);
                if(response != null)
                    break;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 1;
    }

    @Override
    public String routeGetRequest(RequestPayload requestPayload) {
        List<Node> shardGroupNodes = clusterConfig.getShardGroups().get(requestPayload.getShardGroup()).getReplicaNodes();
        String requestPath = String.format("/get/%s",requestPayload.getKey());
        for(Node node : shardGroupNodes){
            String nodeUrl = node.getNodeUrl();
            try {
               String response =  ClientUtils.executeGetRequest(nodeUrl + requestPath, false);
               if(response != null)
                   break;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "Success";
    }


}
