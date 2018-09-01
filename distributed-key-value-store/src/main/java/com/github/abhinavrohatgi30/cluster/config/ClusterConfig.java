package com.github.abhinavrohatgi30.cluster.config;

import com.github.abhinavrohatgi30.cluster.model.Node;
import com.github.abhinavrohatgi30.cluster.model.ShardGroup;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClusterConfig {

    private Map<String, ShardGroup> shardGroups;
    private String myGroup;

    public ClusterConfig(String configPath) throws IOException{
        Yaml clusterConfigYaml  = new Yaml();
        Map<String, List<Map<String, String>>> clusterConfigMap = clusterConfigYaml.loadAs(FileUtils.readFileToString(new File(configPath), Charset.defaultCharset()), Map.class);
        shardGroups = new HashMap<>();
        createClusterConfig(clusterConfigMap);
    }

    private void createClusterConfig(Map<String, List<Map<String, String>>> clusterConfigMap){
        for(Map.Entry<String,List<Map<String,String>>> entry : clusterConfigMap.entrySet()){
            ShardGroup group = new ShardGroup();
            String groupName = entry.getKey();
            List<Node> replicaNodes = entry.getValue().stream().map( m -> new Node(m.get("Host"),m.get("Port"))).collect(Collectors.toList());
            group.setReplicaNodes(replicaNodes);
            group.setGroupSize(replicaNodes.size());
            this.shardGroups.put(groupName,group);
        }
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
}
