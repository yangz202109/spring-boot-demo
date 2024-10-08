package com.study.neo4j.repository;

import com.study.neo4j.model.Lesson;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * <p>
 * 课程节点Repository
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-24 15:05
 */
public interface LessonRepository extends Neo4jRepository<Lesson, String> {
}
