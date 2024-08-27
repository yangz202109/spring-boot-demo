package com.xkcoding.elasticsearch.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Person
 *
 * @author fxbin
 * @version v1.0
 * @since 2019-09-15 23:04
 */
@Document(indexName = "persons")
@Data
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 8510634155374943623L;

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 国家
     */
    private String country;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    @Field(type = FieldType.Date,format = DateFormat.date_hour_minute_second)
    private Date birthday;

    /**
     * 介绍
     */
    private String remark;

    @Field(type = FieldType.Date,format = DateFormat.date_hour_minute_second)
    private Date createTime;

}
