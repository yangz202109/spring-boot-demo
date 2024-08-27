package com.xkcoding.websocket.model.server;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统文件相关信息实体
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-14 16:10
 */
@Setter
@Getter
public class SysFile {
    /**
     * 盘符路径
     */
    private String dirName;

    /**
     * 盘符类型
     */
    private String sysTypeName;

    /**
     * 文件类型
     */
    private String typeName;

    /**
     * 总大小
     */
    private String total;

    /**
     * 剩余大小
     */
    private String free;

    /**
     * 已经使用量
     */
    private String used;

    /**
     * 资源的使用率
     */
    private double usage;

}
