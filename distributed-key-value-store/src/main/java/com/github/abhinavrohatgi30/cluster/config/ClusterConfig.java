package com.github.abhinavrohatgi30.cluster.config;

import com.github.abhinavrohatgi30.cluster.model.Node;
import com.github.abhinavrohatgi30.cluster.model.ShardGroup;
import com.github.abhinavrohatgi30.dao.FileDAO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ClusterConfig {

    private Map<String, ShardGroup> shardGroups;
    private String myGroup;

    private static final Logger logger = LogManager.getLogger(ClusterConfig.class);

    public ClusterConfig(String configPath) throws IOException{
        logger.debug("Config Path ------------->" + configPath);
        shardGroups = new HashMap<>();
        Yaml clusterConfigYaml  = new Yaml();
        Map<String,Object> clusterConfigMap = clusterConfigYaml.loadAs(FileUtils.readFileToString(new File(configPath), Charset.defaultCharset()), Map.class);
        Map<String, List<String>> shardGroupMap = (Map<String, List<String>>) clusterConfigMap.get("ShardGroups");
        this.myGroup = String.valueOf(clusterConfigMap.get("MyGroup"));
        createClusterConfig(shardGroupMap);
    }

    private void createClusterConfig(Map<String, List<String>> shardGroupMap){
        float diff = Integer.MAX_VALUE*1.0f/shardGroupMap.keySet().size();
        float hashFloor = 0.0f;
        for(Map.Entry<String,List<String>> entry : shardGroupMap.entrySet()){
            ShardGroup group = new ShardGroup();
            String groupName = String.valueOf(entry.getKey());
            List<Node> replicaNodes = entry.getValue().stream().map( u -> new Node(u)).collect(Collectors.toList());
            group.setReplicaNodes(replicaNodes);
            group.setGroupSize(replicaNodes.size());
            group.setHashFloor(Math.round(hashFloor));
            hashFloor+=diff;
            this.shardGroups.put(groupName,group);
        }
        logger.info("Cluster Config in Use : %s"+this.toString());
    }

    public Map<String,ShardGroup> getShardGroups(){
        return this.shardGroups;
    }

    public String getMyGroup() {
        return myGroup;
    }

    public void setMyGroup(String myGroup) {
        this.myGroup = myGroup;
    }

    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
