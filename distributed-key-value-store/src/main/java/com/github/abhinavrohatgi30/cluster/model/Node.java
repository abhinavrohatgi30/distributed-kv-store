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

    @Override
    public boolean equals(Object obj) {
        Node node = (Node)obj;
        if(node.getHost() == this.getHost() && node.getPort() == this.getPort()){
            return true;
        }
        return false;
    }
}
