package com.github.abhinavrohatgi30.cluster.model;

import java.util.List;

public class ShardGroup {

    private List<Node> replicaNodes;
    private int groupSize;

    public List<Node> getReplicaNodes() {
        return replicaNodes;
    }

    public void setReplicaNodes(List<Node> replicaNodes) {
        this.replicaNodes = replicaNodes;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }
}