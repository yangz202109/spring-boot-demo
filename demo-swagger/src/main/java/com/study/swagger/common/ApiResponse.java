package com.study.swagger.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 通用API接口返回
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-29 11:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "通用PI接口返回", description = "Common Api Response")
public class ApiResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -8987146499044811408L;
    /**
     * 通用返回状态
     */
    @Schema(description = "通用返回状态码", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;
    /**
     * 通用返回信息
     */
    @Schema(description = "通用返回信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
    /**
     * 通用返回数据
     */
    @Schema(description = "通用返回数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private T data;
}
