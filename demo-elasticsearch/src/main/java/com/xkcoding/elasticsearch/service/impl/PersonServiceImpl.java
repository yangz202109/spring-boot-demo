package com.xkcoding.elasticsearch.service.impl;

import cn.hutool.core.util.StrUtil;
import com.xkcoding.elasticsearch.entity.Person;
import com.xkcoding.elasticsearch.repository.PersonRepository;
import com.xkcoding.elasticsearch.service.PersonService;
import com.xkcoding.elasticsearch.vo.PersonRequestVO;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query是一个接口，Spring Data Elasticsearch提供了三种实现：CriteriaQuery、StringQuery和NativeQuery。
 *
 */

@Service
public class PersonServiceImpl implements PersonService {
    @Resource
    private PersonRepository personRepository;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }

    @Override
    public void del(Long id) {
      personRepository.deleteById(id);
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @Override
    public Map<String, Object> search(PersonRequestVO personRequestVO) {
        String name = personRequestVO.getName();
        String country = personRequestVO.getCountry();
        Integer age = personRequestVO.getAge();
        Integer pageNum = personRequestVO.getPageNum();
        Integer pageSize = personRequestVO.getPageSize();

        Criteria criteria = new Criteria();
        if (StrUtil.isNotEmpty(name)) {
           criteria.and("name").contains(name);
        }
        if (StrUtil.isNotEmpty(country)){
            criteria.and("country").is(country);
        }
        if (age != null){
            criteria.and("age").is(age);
        }

        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        criteriaQuery.setPageable(PageRequest.of(pageNum - 1, pageSize))
                .addSort(Sort.by("createTime").descending());

        SearchHits<Person> searchHits = elasticsearchTemplate.search(criteriaQuery, Person.class);
        List<Person> people = searchHits.get().map(SearchHit::getContent).toList();

        Map<String, Object> result = HashMap.newHashMap(4);
        result.put("current",pageNum);
        result.put("size",pageSize);
        result.put("total",searchHits.getTotalHits());
        result.put("list",people);
        return  result;
    }
}
