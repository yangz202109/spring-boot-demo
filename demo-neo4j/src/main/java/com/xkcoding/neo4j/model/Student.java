package com.xkcoding.neo4j.model;

import com.xkcoding.neo4j.config.CustomIdStrategy;
import com.xkcoding.neo4j.constants.NeoConst;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import java.util.List;

/**
 * <p>
 * 学生节点
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-24 14:38
 */
@Data
@Builder
@Node
public class Student {
    /**
     * 主键，自定义主键策略，使用UUID生成
     */
    @Id
    @GeneratedValue(CustomIdStrategy.class)
    private String id;

    /**
     * 学生姓名
     */
    @NonNull
    private String name;

    /**
     * 学生选的所有课程
     */
    @Relationship(NeoConst.R_LESSON_OF_STUDENT)
    @NonNull
    private List<Lesson> lessons;

    /**
     * 学生所在班级
     */
    @Relationship(NeoConst.R_STUDENT_OF_CLASS)
    @NonNull
    private Class clazz;

    public Student() {
    }

    public Student(@NonNull String name, @NonNull List<Lesson> lessons, @NonNull Class clazz) {
        this.name = name;
        this.lessons = lessons;
        this.clazz = clazz;
    }

    public Student(String id, @NonNull String name, @NonNull List<Lesson> lessons, @NonNull Class clazz) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
        this.clazz = clazz;
    }
}
