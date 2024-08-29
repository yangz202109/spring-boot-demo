package com.study.websocket.model.server;

import lombok.Getter;

/**
 * <p>
 * 系统相关信息实体
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-14 16:10
 */
@Getter
public class Sys {
    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    private String computerIp;

    /**
     * 项目路径
     */
    private String userDir;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public void setComputerIp(String computerIp) {
        this.computerIp = computerIp;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }
}
