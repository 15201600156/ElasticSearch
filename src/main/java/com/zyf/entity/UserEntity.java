package com.zyf.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "mymayikt", type = "user" )
@Data
public class UserEntity {
    @Id
    private String id;
    @Field
    private String name;
    private int sex;
    private int age;
}