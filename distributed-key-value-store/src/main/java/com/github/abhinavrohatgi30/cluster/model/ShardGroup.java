package com.github.abhinavrohatgi30.cluster.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class ShardGroup {

    private List<Node> replicaNodes;
    private int groupSize;
    private int hashFloor;

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

    public int getHashFloor() {
        return hashFloor;
    }

    public void setHashFloor(int hashFloor) {
        this.hashFloor = hashFloor;
    }

    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
