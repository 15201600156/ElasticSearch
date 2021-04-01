package com.zyf;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.zyf.dao")
public class ElasticSearchApplication {
        public  static  void  main (String[] arg){
            SpringApplication.run(ElasticSearchApplication.class,arg);
        }
}
