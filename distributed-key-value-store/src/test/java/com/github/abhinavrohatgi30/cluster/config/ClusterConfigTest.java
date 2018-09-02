package com.github.abhinavrohatgi30.cluster.config;

import com.github.abhinavrohatgi30.cluster.model.Node;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClusterConfigTest {


    @Test
    public void constructor_shouldCreateAClusterConfig_WhenAValidConfigFileIsProvided() throws IOException{
        String validConfigPath = "../cluster-conf.yaml";
        ClusterConfig clusterConfig = new ClusterConfig(validConfigPath);
        assertEquals(clusterConfig.getShardGroups().size(),2);
        assertEquals(clusterConfig.getMyGroup(),"1");
//        assertTrue(clusterConfig.getShardGroups().get("1").getReplicaNodes().contains(new Node("localhost","8900")));
//        assertTrue(clusterConfig.getShardGroups().get("1").getReplicaNodes().contains(new Node("localhost","8910")));
//        assertTrue(clusterConfig.getShardGroups().get("2").getReplicaNodes().contains(new Node("localhost","8800")));
//        assertTrue(clusterConfig.getShardGroups().get("2").getReplicaNodes().contains(new Node("localhost","8810")));
    }


    @Test(expected = IOException.class)
    public void constructor_shouldThrowAnException_WhenAnInValidConfigFileIsProvided() throws IOException{
        String validConfigPath = "../cluster-conf_3.yaml";
        ClusterConfig clusterConfig = new ClusterConfig(validConfigPath);
    }

}
