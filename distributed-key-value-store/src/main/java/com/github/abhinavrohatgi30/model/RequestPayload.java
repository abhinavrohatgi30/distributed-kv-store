package com.github.abhinavrohatgi30.model;

import com.github.abhinavrohatgi30.util.RouteRequestType;

public class RequestPayload {
    private String key;
    private String value;
    private RouteRequestType requestType;
    private String shardGroup;

    public RequestPayload(String key, String value){
        this.key = key;
        this.value = value;
        this.requestType = RouteRequestType.PUT;
    }

    public RequestPayload(String key){
        this.key = key;
        this.requestType = RouteRequestType.GET;
    }


    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public RouteRequestType getRequestType() {
        return requestType;
    }

    public String getShardGroup() {
        return shardGroup;
    }

    public void setShardGroup(String shardGroup) {
        this.shardGroup = shardGroup;
    }
}
