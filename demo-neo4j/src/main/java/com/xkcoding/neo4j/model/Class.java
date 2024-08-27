package com.xkcoding.neo4j.model;

import com.xkcoding.neo4j.config.CustomIdStrategy;
import com.xkcoding.neo4j.constants.NeoConst;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * <p>
 * 班级节点
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-24 14:44
 */
@Data
@Builder
@Node
public class Class {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(CustomIdStrategy.class)
    private String id;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 班级的班主任
     */
    @Relationship(NeoConst.R_BOSS_OF_CLASS)
    private Teacher boss;

    public Class() {
    }

    public Class(String name, Teacher boss) {
        this.name = name;
        this.boss = boss;
    }

    public Class(String id, String name, Teacher boss) {
        this.id = id;
        this.name = name;
        this.boss = boss;
    }
}
