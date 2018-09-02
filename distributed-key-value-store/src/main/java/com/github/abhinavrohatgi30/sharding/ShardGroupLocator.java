package com.github.abhinavrohatgi30.sharding;

public interface ShardGroupLocator {

    String locate(String key);
}
