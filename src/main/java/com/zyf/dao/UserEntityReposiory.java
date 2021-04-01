package com.zyf.dao;

import com.zyf.entity.UserEntity;
//import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserEntityReposiory  extends ElasticsearchRepository<UserEntity,String> {

}
