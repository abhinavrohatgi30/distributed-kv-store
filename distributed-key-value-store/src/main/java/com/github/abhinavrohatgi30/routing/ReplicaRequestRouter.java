package com.github.abhinavrohatgi30.routing;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.cluster.model.Node;
import com.github.abhinavrohatgi30.dao.DAO;
import com.github.abhinavrohatgi30.model.RequestPayload;
import com.github.abhinavrohatgi30.util.ClientUtils;
import com.github.abhinavrohatgi30.util.ResponseMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ReplicaRequestRouter implements RequestRouter {

    private List<Node> replicas;
    private String myGroup;

    private static final Logger logger = LogManager.getLogger(ReplicaRequestRouter.class);



    public ReplicaRequestRouter(ClusterConfig clusterConfig){
       this.replicas = clusterConfig.getShardGroups().get(clusterConfig.getMyGroup()).getReplicaNodes();
       this.myGroup = clusterConfig.getMyGroup();
    }


    @Override
    public String routePutRequest(RequestPayload requestPayload) {
        String requestPath = String.format("/set/%s",requestPayload.getKey());
        JSONObject messageObject = new JSONObject();
        for(Node node : replicas){
            String nodeUrl = node.getNodeUrl();
            logger.debug(String.format("Routing the request to the following URL : %s",nodeUrl+requestPath));
            try {
                String response = ClientUtils.executePostRequest(nodeUrl + requestPath,requestPayload.getValue(), true);
                if(response != null) {
                    JSONObject responseJson = new JSONObject(response);
                    String message = responseJson.getString("message");
                    messageObject.put(nodeUrl,message);
                }
            }catch (Exception e){
                e.printStackTrace();
                try {
                    messageObject.put(nodeUrl, ResponseMessage.NODE_NOT_AVAILABLE);
                }catch (Exception e1){}
                logger.error(String.format("Route to the following URL : %s failed", nodeUrl + requestPath));
            }
        }
        return messageObject.toString();
    }

    @Override
    public String routeGetRequest(RequestPayload requestPayload) {
        String requestPath = String.format("/get/%s",requestPayload.getKey());
        for(Node node : replicas){
            String nodeUrl = node.getNodeUrl();
            logger.debug(String.format("Routing the request to the following URL : %s",nodeUrl+requestPath));
            try {
                String response =  ClientUtils.executeGetRequest(nodeUrl + requestPath, true);
                if(response != null) {
                    JSONObject responseJson = new JSONObject(response);
                    return responseJson.getString("message");
                }
            }catch (Exception e){
               e.printStackTrace();
                logger.error(String.format("Route to the following URL : %s failed", nodeUrl + requestPath));
            }
       }
       return String.format(ResponseMessage.NO_NODES_TO_HANDLE_SHARDS,this.myGroup);
    }
}
