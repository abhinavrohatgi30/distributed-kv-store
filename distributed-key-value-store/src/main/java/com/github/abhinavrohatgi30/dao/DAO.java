package com.github.abhinavrohatgi30.dao;

public interface DAO {

    String read(String key);

    int write(String key, String value);

    int delete(String key);
}
