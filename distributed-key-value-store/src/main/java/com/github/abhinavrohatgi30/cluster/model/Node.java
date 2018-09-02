package com.github.abhinavrohatgi30.cluster.model;

public class Node {
    private String host;
    private String port;

    public Node(String host, String port){
        this.host = host;
        this.port = port;
    }

    public Node(String url){
        String[] parts = url.split("\\:");
        this.host = parts[0];
        this.port = parts[1];
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getNodeUrl(){
        return String.format("http://%s:%s",host,port);
    }

}
