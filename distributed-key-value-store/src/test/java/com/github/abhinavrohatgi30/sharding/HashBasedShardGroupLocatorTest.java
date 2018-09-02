package com.github.abhinavrohatgi30.sharding;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HashBasedShardGroupLocatorTest {

    private HashBasedShardGroupLocator shardGroupLocator;

    @Before
    public void init() throws IOException{
        ClusterConfig clusterConfig = new ClusterConfig("src/test/resources/cluster-conf-test.yaml");
        shardGroupLocator = new HashBasedShardGroupLocator(clusterConfig);
    }

    @Test
    public void constructor_shouldCreateANavigableMap_withFloorValueOfEachHashPartition() throws IOException{
        ClusterConfig clusterConfig = new ClusterConfig("src/test/resources/cluster-conf-test.yaml");
        shardGroupLocator = new HashBasedShardGroupLocator(clusterConfig);
        assertEquals(shardGroupLocator.getShardGroupLookupMap().size(),4);
        assertEquals(shardGroupLocator.getShardGroupLookupMap().get(clusterConfig.getShardGroups().get("1").getHashFloor()),"1");
        assertEquals(shardGroupLocator.getShardGroupLookupMap().get(clusterConfig.getShardGroups().get("2").getHashFloor()),"2");
        assertEquals(shardGroupLocator.getShardGroupLookupMap().get(clusterConfig.getShardGroups().get("3").getHashFloor()),"3");
        assertEquals(shardGroupLocator.getShardGroupLookupMap().get(clusterConfig.getShardGroups().get("4").getHashFloor()),"4");
    }


    @Test
    public void locate_shouldReturnTheCorrectShardGroup_whenKeyIsProvided() throws IOException{
        ClusterConfig clusterConfig = new ClusterConfig("src/test/resources/cluster-conf-test.yaml");
        shardGroupLocator = new HashBasedShardGroupLocator(clusterConfig);
        assertEquals(shardGroupLocator.locate("abc"),"1");
        assertEquals(shardGroupLocator.locate("test-najsndlasmdlkmaslkd"),"2");
        assertEquals(shardGroupLocator.locate("zzzzzasds-sdasdasdsadnaskjdnasjndasnld"),"4");
    }


}
