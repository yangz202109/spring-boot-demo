package com.xkcoding.neo4j.model;

import com.xkcoding.neo4j.config.CustomIdStrategy;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * <p>
 * 教师节点
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-24 14:54
 */
@Data
@Builder
@Node
public class Teacher {
    /**
     * 主键，自定义主键策略，使用UUID生成
     */
    @Id
    @GeneratedValue(generatorClass = CustomIdStrategy.class)
    private String id;

    /**
     * 教师姓名
     */
    private String name;

    public Teacher() {
    }

    public Teacher(@NonNull String name) {
        this.name = name;
    }

    public Teacher(String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
