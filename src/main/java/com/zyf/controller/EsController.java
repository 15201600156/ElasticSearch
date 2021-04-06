package com.zyf.controller;

import com.zyf.dao.UserEntityReposiory;
import com.zyf.dao.UserReposiory;
import com.zyf.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.hibernate.validator.resourceloading.AggregateResourceBundleLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/es")
@Slf4j
public class EsController {

    @Autowired
    private UserReposiory userReposiory;

    @Autowired
    private UserEntityReposiory userEntityReposiory;


    @RequestMapping("/addUser")
    public UserEntity addUser(UserEntity user) {
        return userReposiory.save(user);
    }

    @RequestMapping("/findUser")
    public UserEntity findUser(String id) {
        return userReposiory.findById(id).get();
    }

    //region  精确查询term适合于date、num、id等确切数据进行搜索

    /**
     * term查询：主要是精确匹配单个或者多条词条
     *
     * @return
     */
    @RequestMapping("/findTermQuery")
    public List<UserEntity> findTermQuery(String columnName,String columnValue) {

        QueryBuilder builder = QueryBuilders.termQuery(columnName, columnValue);
        Iterable<UserEntity> search = userEntityReposiory.search(builder);
        List<UserEntity> userEntities = new ArrayList<>();
//        search.forEach(w -> userEntities.add(w));
        Iterator<UserEntity> iterator = search.iterator();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;

    }


    /**
     * terms查询：主要是精确匹配单个或者多条词条   多个参数里面只要有一个能匹配上就会将该条数据加载出来
     *
     * @return
     */
    @RequestMapping("/findTermsQuery")
    public List<UserEntity> findTermsQuery(String name) {

        QueryBuilder builder = QueryBuilders.termsQuery("age", name, 50);
        Iterable<UserEntity> search = userEntityReposiory.search(builder);

        List<UserEntity> userEntities = new ArrayList<>();
        Iterator<UserEntity> iterator = search.iterator();
//        search.forEach(w -> userEntities.add(w));
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }


    //endregion

    //region 如果需要查询keywords，则查询等keywords必须是查询字段中可以分出来的词，如果不是，则无法查询到数据。

    /**
     * term查询：主要是精确匹配单个或者多条词条
     *
     * @return
     */
    @RequestMapping("/findTermQueryKeyword")
    public List<UserEntity> findTermQueryKeyword(String name) {

        QueryBuilder builder = QueryBuilders.termQuery("name.keyword", name);
        Iterable<UserEntity> search = userEntityReposiory.search(builder);
        List<UserEntity> userEntities = new ArrayList<>();
//        search.forEach(w -> userEntities.add(w));
        Iterator<UserEntity> iterator = search.iterator();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;

    }


    /**
     * terms查询：主要是精确匹配单个或者多条词条   多个参数里面只要有一个能匹配上就会将该条数据加载出来
     *
     * @return
     */
    @RequestMapping("/findTermsQueryKeyword")
    public List<UserEntity> findTermsQueryKeyword(String name) {
        QueryBuilder builder = QueryBuilders.termsQuery("name.keyword", name);
        Iterable<UserEntity> search = userEntityReposiory.search(builder);
        List<UserEntity> userEntities = new ArrayList<>();
        Iterator<UserEntity> iterator = search.iterator();
//        search.forEach(w -> userEntities.add(w));
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }
    //endregion

    //region 模糊查询 现在有问题
    /**
     * 模糊查询
     * @param name
     * @return
     */
    @RequestMapping("/moreLikeThisQuery")
    public List<UserEntity> moreLikeThisQuery(String[] name) {
        log.info("进入方法");
        QueryBuilder queryBuilder = QueryBuilders.moreLikeThisQuery(new String[]{"name"}, new String[]{"as"},null);
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }
    //endregion


    //#region 范围查询

    @RequestMapping("/rangeQuery")
    public List<UserEntity> rangeQuery(String age1,String age2) {
        log.info("进入方法");
//        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").from(age1).to(age2);  //闭区间查询 包含两个边界

//        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").from(age1,false).to(age2,false);  //开区间查询 不包含两个边界
//        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").gt(age1); //大于此值的 不包含此值
//        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").gte(age1);   //大于等于

//        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").lt(age1);;   //小于
//
//
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").lte(age1);;   //小于等于
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }

    //#endregion


    //#region 匹配某某开头的   //中文只能匹配首字符  英文写多少匹配多少matchPhrasePrefixQuery
    @RequestMapping("/prefixQuery")
    public List<UserEntity> prefixQuery(String name) {
        QueryBuilder queryBuilder=QueryBuilders.prefixQuery("name",name);
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
   }
    //#endregion






    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @RequestMapping("/aggregationBuilder")
    public List<UserEntity> aggregationBuilder() {

       MaxAggregationBuilder field = AggregationBuilders.max("max_age").field("age");


        NativeSearchQueryBuilder searchQuery=new NativeSearchQueryBuilder();
        searchQuery.withQuery(QueryBuilders.matchAllQuery());
        searchQuery.addAggregation(field);



        List<UserEntity> queryForList = elasticsearchTemplate.queryForList(searchQuery.build(), UserEntity.class);
        //3.3方法3,通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery.build(), new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });


        Iterable<UserEntity> search = userEntityReposiory.search(searchQuery.build());
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }
}
