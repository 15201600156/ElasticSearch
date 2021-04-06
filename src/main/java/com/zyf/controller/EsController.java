package com.zyf.controller;

import com.zyf.dao.UserEntityReposiory;
import com.zyf.dao.UserReposiory;
import com.zyf.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * TermQueryBuilder 对词条进行完全匹配   计算机=计算机
 * WildcardQueryBuilder  对词条进行模糊匹配  "计算机" =  *算*
 * QueryStringQueryBuilder 对查询的词进行分词 再进行匹配
 */
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



    @RequestMapping("/matchAllQuery")
    public List<UserEntity> matchAllQuery() {
        QueryBuilder queryBuilder=QueryBuilders.matchAllQuery();
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }

    @RequestMapping("/matchQuery")
    public List<UserEntity> matchQuery() {
        QueryBuilder queryBuilder=QueryBuilders.matchQuery("name","张");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }

    /**
     * 搜索一个值，但是指定多个字段查询
     * @return
     */
    @RequestMapping("/multiMatchQuery")
    public List<UserEntity> multiMatchQuery() {
        QueryBuilder queryBuilder=QueryBuilders.multiMatchQuery("张","name");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }

    /**
     * 通配符 查询
     * *代表0个或多个字符
     * ？代表任意一个字符
     * @return
     */
    @RequestMapping("/wildcardQuery")
    public List<UserEntity> wildcardQuery() {
        QueryBuilder queryBuilder=QueryBuilders.wildcardQuery("name","张*");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }

    /**
     * 模糊查询
     * @return
     */
    @RequestMapping("/fuzzyQuery")
    public List<UserEntity> fuzzyQuery() {
        QueryBuilder queryBuilder=QueryBuilders.fuzzyQuery("name","张");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }

    /**
     * 类型查询
     * @return
     */
    @RequestMapping("/typeQuery")
    public List<UserEntity> typeQuery() {
        QueryBuilder queryBuilder=QueryBuilders.typeQuery("user");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }


    /**
     * 多个ID查询
     * @return
     */
    @RequestMapping("/idsQuery")
    public List<UserEntity> idsQuery() {
        QueryBuilder queryBuilder=QueryBuilders.idsQuery("user").addIds("1","2","3","4","5","6","7");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }

    /**
     * 聚合查询 没有实现 明天查
     * @return
     */
    @RequestMapping("/aggregationBuilders")
    public List<UserEntity> aggregationBuilders() {
        NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder();
        //  MaxAggregationBuilder builder= AggregationBuilders.max("aggMax").field("age"); //求age的最大值
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.max("agemax").field("age"));
        Iterable<UserEntity> search = userEntityReposiory.search(nativeSearchQueryBuilder.build());
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }



    /**
     * 查询  哪个地方用需要查询一下
     * @return
     */
    @RequestMapping("/commonTermsQuery")
    public List<UserEntity> commonTermsQuery() {
        QueryBuilder queryBuilder=QueryBuilders.commonTermsQuery("name","张");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }

    /**
     * 全文搜索 不需要写字段   +张 代表含有张的  -张 不含有张的      +张 -军  就是含有张 不含有军的        完全满足这个条件的才会出来
     * @return
     */
    @RequestMapping("/queryStringQuery")
    public List<UserEntity> queryStringQuery() {
        QueryBuilder queryBuilder=QueryBuilders.queryStringQuery("+张 -军");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }
    /**
     * 全文搜索 不需要写字段   +张 代表含有张的  -张 不含有张的      +张 -军  就是含有张 不含有军的        只需要满足一个就可以
     * @return
     */
    @RequestMapping("/simpleQueryStringQuery")
    public List<UserEntity> simpleQueryStringQuery() {
        QueryBuilder queryBuilder=QueryBuilders.simpleQueryStringQuery("+a -张");
        Iterable<UserEntity> search = userEntityReposiory.search(queryBuilder);
        Iterator<UserEntity> iterator = search.iterator();
        List<UserEntity> userEntities = new ArrayList<>();
        while (iterator.hasNext()) {
            userEntities.add(iterator.next());
        }
        return userEntities;
    }
}

