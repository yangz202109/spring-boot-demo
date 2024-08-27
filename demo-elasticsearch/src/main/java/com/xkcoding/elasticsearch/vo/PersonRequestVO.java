package com.xkcoding.elasticsearch.vo;

import lombok.Data;

@Data
public class PersonRequestVO {

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

    private Integer pageNum;

    private Integer pageSize;

}
