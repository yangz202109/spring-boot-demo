package com.study.neo4j.payload;

import com.study.neo4j.model.Student;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import java.util.List;

/**
 * <p>
 * 按照课程分组的同学关系
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-24 19:18
 */
@RelationshipProperties
@Data
public class ClassmateInfoGroupByLesson {
    /**
     * 课程名称
     */
    private String lessonName;

    /**
     * 学生信息
     */
    private List<Student> students;
}
