package com.genture.simulatePlatform.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * Created by Administrator on 2018/1/6.
 */
@Configuration
@Import(MongoDBConfig.class)
public class MongoTemplateConfig {
    @Primary
    @Bean
    @Qualifier("mongoDbFactory")
    public MongoDbFactory mongoDbFactory(@Value("${spring.data.mongodb.main.database}") String database, @Qualifier("primaryDataSource") MongoClient mongoClient){
        MongoDbFactory factory = new SimpleMongoDbFactory(mongoClient, database);
        return factory;
    }

    @Primary
    @Bean
    public MongoTemplate mongoTemplate(@Qualifier("mongoDbFactory") MongoDbFactory factory){
        return new MongoTemplate(factory);
    }
}
