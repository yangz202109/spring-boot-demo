package com.xkcoding.elasticsearch;

import com.xkcoding.elasticsearch.entity.Person;
import com.xkcoding.elasticsearch.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.*;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ElasticsearchApplicationTests {

    @Autowired
    private PersonService personService;

    @Autowired
    private IndicesTemplate indicesTemplate;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * 测试新增
     */
    @Test
    public void insertTest() {
        Person person = new Person();
        person.setId(111L);
        person.setAge(23);
        person.setName("tom");
        person.setCountry("UK");
        person.setBirthday(new Date());
        person.setCreateTime(new Date());
        personService.save(person);
    }


    /**
     * 测试删除
     */
    @Test
    public void deleteTest() {
        personService.del(111L);
    }

    /**
     * 使用 CriteriaQuery
     * 允许用户通过简单地链接和组合指定搜索文档必须满足的条件的Criteria对象来构建查询。
     */
    @Test
    public void criteriaQuery() {
        //查询年龄在18到30岁的用户
        Criteria criteria = new Criteria("age").greaterThan(18).lessThan(30);

        //查询年龄在18以上并且国籍为UK的用户  and 创建一个新的Criteria并将其链接到第一个。
        Criteria criteria1 = new Criteria("age").greaterThan(18).and("country").is("UK");

        SearchHits<Person> searchHits = elasticsearchTemplate.search(new CriteriaQuery(criteria1), Person.class);
        List<Person> people = searchHits.get().map(SearchHit::getContent).toList();
        System.out.println(people);
    }


    /**
     * 使用 StringQuery
     * 此类将Elasticsearch查询作为JSON字符串。
     */
    @Test
    public void stringQuery() {
        //查询国籍为UK的用户
        Query query = new StringQuery("{ \"match\": { \"country\": { \"query\": \"UK\" } } } ");

        SearchHits<Person> searchHits = elasticsearchTemplate.search(query, Person.class);
        List<Person> people = searchHits.get().map(SearchHit::getContent).toList();
        System.out.println(people);
    }

    /**
     * 使用 NativeQuery
     * NativeQuery是当您有复杂查询或无法使用CriteriaAPI表示的查询时使用的类，例如在构建查询和使用聚合时。
     * 它允许使用所有不同的co.elastic.clients.elasticsearch._types.query_dsl.Query实现从Elasticsearch库因此命名为"本地"。
     */
    @Test
    public void nativeQuery() {
        String name = "tom";
        String country = "US";
        Integer age = 18;

        Criteria criteria = Criteria.where("name").contains(name).and("country").is(country).and("age").is(age);

        Query query = NativeQuery.builder()
                .withQuery(new CriteriaQuery(criteria))
                .withPageable(PageRequest.of(0, 10))
                .withSort(Sort.by("createTime").descending())
                .build();
        SearchHits<Person> searchHits = elasticsearchTemplate.search(query, Person.class);
        List<Person> people = searchHits.get().map(SearchHit::getContent).toList();
        System.out.println(people);
    }

}
