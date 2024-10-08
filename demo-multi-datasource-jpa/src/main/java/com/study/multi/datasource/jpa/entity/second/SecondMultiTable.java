package com.study.multi.datasource.jpa.entity.second;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <p>
 * 多数据源测试表
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-01-18 10:06
 */
@Data
@Entity
@Table(name = "multi_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecondMultiTable {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 名称
     */
    private String name;
}
