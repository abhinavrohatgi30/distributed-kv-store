package com.github.abhinavrohatgi30.routing;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.cluster.model.Node;
import com.github.abhinavrohatgi30.model.RequestPayload;
import com.github.abhinavrohatgi30.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReplicaRequestRouter implements RequestRouter {

    @Autowired
    private ClusterConfig clusterConfig;

    private List<Node> replicas;


    public ReplicaRequestRouter(){
        replicas = clusterConfig.getShardGroups().get(clusterConfig.getMyGroup()).getReplicaNodes();
    }


    @Override
    public int routePutRequest(RequestPayload requestPayload) {
        String requestPath = String.format("/set/%s",requestPayload.getKey());
        for(Node node : replicas){
            String nodeUrl = node.getNodeUrl();
            try {
                ClientUtils.executePostRequest(nodeUrl + requestPath,requestPayload.getValue(), true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 1;
    }

    @Override
    public String routeGetRequest(RequestPayload requestPayload) {
        String requestPath = String.format("/get/%s",requestPayload.getKey());
        for(Node node : replicas){
           String nodeUrl = node.getNodeUrl();
           try {
               ClientUtils.executeGetRequest(nodeUrl + requestPath, true);
           }catch (Exception e){
               e.printStackTrace();
           }
       }
       return "Success";
    }
}
