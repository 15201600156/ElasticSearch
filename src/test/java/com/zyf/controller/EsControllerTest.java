package com.zyf.controller;

import com.zyf.dao.UserEntityReposiory;
import com.zyf.entity.UserEntity;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@RunWith(Arquillian.class)
public class EsControllerTest {
//    @Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//                .addClass(EsController.class)
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }

    @Autowired
    UserEntityReposiory userEntityReposiory;
    @Test
    public void findTermQuery() {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        System.out.println(search);
    }
}
