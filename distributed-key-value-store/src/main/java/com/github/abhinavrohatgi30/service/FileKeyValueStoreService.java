package com.github.abhinavrohatgi30.service;

import com.github.abhinavrohatgi30.dao.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileKeyValueStoreService implements  KeyValueStoreService {

    @Autowired
    private DAO dao;

    @Override
    public String getValue(String key) {
        return dao.read(key);
    }

    @Override
    public int indexKeyValue(String key, String value) {
        return dao.write(key,value);
    }
}
