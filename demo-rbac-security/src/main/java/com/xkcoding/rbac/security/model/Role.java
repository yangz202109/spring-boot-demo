package com.xkcoding.rbac.security.model;

import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-07 15:45
 */
@Data
@Entity
@Table(name = "sec_role")
public class Role {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Long updateTime;
}
