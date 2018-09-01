package com.github.abhinavrohatgi30.cluster.model;

public class Node {
    private String host;
    private String port;
    private NodeType nodeType;

    public Node(String host, String port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType){
        this.nodeType = nodeType;
    }

}
