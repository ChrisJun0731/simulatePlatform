package com.genture.simulatePlatform.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2018/1/6.
 */
@Configuration
public class MongoDBConfig {
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    public MongoClient primaryMongoSource(@Value("${spring.data.mongodb.main.host}") String host, @Value("${spring.data.mongodb.main.port}") int port){
        return new MongoClient(host, port);
    }

}
