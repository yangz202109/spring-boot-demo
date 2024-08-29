package com.study.elasticsearch.service;

import com.study.elasticsearch.entity.Person;
import com.study.elasticsearch.vo.PersonRequestVO;
import java.util.Map;

public interface PersonService {

    void save(Person person);

    void del(Long id);

    Person findById(Long id);

    /**
     * 根据名称,国家和年龄分页查询
     *
     * @param personRequestVO 请求参数vo
     */
    Map<String, Object> search(PersonRequestVO personRequestVO);
}
