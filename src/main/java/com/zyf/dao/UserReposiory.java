package com.zyf.dao;

import com.zyf.entity.UserEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface UserReposiory extends ElasticsearchCrudRepository<UserEntity, String>
{

}
