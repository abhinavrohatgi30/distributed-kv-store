package com.github.abhinavrohatgi30;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.dao.DAO;
import com.github.abhinavrohatgi30.dao.FileDAO;
import com.github.abhinavrohatgi30.dao.WriteAheadLogDAO;
import com.github.abhinavrohatgi30.routing.ShardRequestRouter;
import com.github.abhinavrohatgi30.service.FileKeyValueStoreService;
import com.github.abhinavrohatgi30.service.KeyValueStoreService;
import com.github.abhinavrohatgi30.sharding.HashBasedShardGroupLocator;
import com.github.abhinavrohatgi30.sharding.ShardGroupLocator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public ClusterConfig clusterConfig(@Value("${clusterConfig}") String clusterConfigPath) throws IOException{
        return new ClusterConfig(clusterConfigPath);
    }

    @Bean
    public KeyValueStoreService keyValueStoreService(){
        return new FileKeyValueStoreService();
    }

    @Bean(name = "fileDAO")
    public DAO fileDAO(@Value("${baseFolder}") String baseFolder) throws IOException{
        return new FileDAO(baseFolder+ File.separator+"data");
    }

    @Bean(name = "walDAO")
    public DAO walDAO(@Value("${baseFolder}") String baseFolder) throws IOException{
        return new WriteAheadLogDAO(baseFolder+ File.separator+"wal");
    }

}
