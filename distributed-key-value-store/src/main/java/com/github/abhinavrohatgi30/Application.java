package com.github.abhinavrohatgi30;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.dao.DAO;
import com.github.abhinavrohatgi30.dao.FileDAO;
import com.github.abhinavrohatgi30.service.FileKeyValueStoreService;
import com.github.abhinavrohatgi30.service.KeyValueStoreService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
@ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public ClusterConfig clusterConfig() throws IOException{
        return new ClusterConfig("cluster-conf.yaml");
    }

    @Bean
    public KeyValueStoreService keyValueStoreService(){
        return new FileKeyValueStoreService();
    }

    @Bean
    public DAO fileDAO() throws IOException{
        return new FileDAO("test-new");
    }
}
