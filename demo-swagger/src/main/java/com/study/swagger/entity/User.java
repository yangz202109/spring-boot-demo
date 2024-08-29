package com.study.swagger.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 用户实体
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-29 11:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户实体", description = "User Entity")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 5057954049311281252L;
    /**
     * 主键id
     */
    @Schema(name = "主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
    /**
     * 用户名
     */
    @Schema(name = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    /**
     * 工作岗位
     */
    @Schema(name = "工作岗位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String job;
}
