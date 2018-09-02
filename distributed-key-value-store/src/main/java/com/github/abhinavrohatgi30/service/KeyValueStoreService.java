package com.github.abhinavrohatgi30.service;

public interface KeyValueStoreService {

    String getValue(String key);
    int indexKeyValue(String key, String value,Boolean isRouted);
}
