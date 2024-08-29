package com.study.neo4j.payload;

import com.study.neo4j.model.Student;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import java.util.List;

/**
 * <p>
 * 师生关系
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-24 19:18
 */
@Data
@RelationshipProperties
public class TeacherStudent {
    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 学生信息
     */
    private List<Student> students;
}
