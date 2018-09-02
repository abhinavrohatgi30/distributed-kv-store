package com.github.abhinavrohatgi30.dao;

public interface DAO {

    String read(String key);

    String write(String key, String value);

    int delete(String key);
}
