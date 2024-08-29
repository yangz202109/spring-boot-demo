package com.study.neo4j.model;

import com.study.neo4j.config.CustomIdStrategy;
import com.study.neo4j.constants.NeoConst;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * <p>
 * 课程节点
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-24 14:55
 */
@Data
@Builder
@Node
public class Lesson {
    /**
     * 主键，自定义主键策略，使用UUID生成
     */
    @Id
    @GeneratedValue(CustomIdStrategy.class)
    private String id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 任教老师
     */
    @Relationship(NeoConst.R_TEACHER_OF_LESSON)
    private Teacher teacher;

    public Lesson() {
    }

    public Lesson(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher;
    }

    public Lesson(String id, String name, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
    }
}
