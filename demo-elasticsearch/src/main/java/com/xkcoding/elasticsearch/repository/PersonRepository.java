package com.xkcoding.elasticsearch.repository;

import com.xkcoding.elasticsearch.entity.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ElasticsearchRepository<Person, Long> {
}
