package com.study.websocket.util;

import cn.hutool.core.lang.Dict;
import com.study.websocket.model.Server;
import com.study.websocket.payload.ServerVO;

/**
 * <p>
 * 服务器转换工具类
 * </p>
 *
 * @author yangz
 * @date Created in 2018-12-17 10:24
 */
public class ServerUtil {
    /**
     * 包装成 ServerVO
     *
     * @param server server
     * @return ServerVO
     */
    public static ServerVO wrapServerVO(Server server) {
        ServerVO serverVO = new ServerVO();
        serverVO.create(server);
        return serverVO;
    }

    /**
     * 包装成 Dict
     *
     * @param serverVO serverVO
     * @return Dict
     */
    public static Dict wrapServerDict(ServerVO serverVO) {
        return Dict.create().set("cpu", serverVO.getCpu().getFirst().getData()).set("mem",
                serverVO.getMem().getFirst().getData()).set("sys",
                serverVO.getSys().getFirst().getData()).set("jvm",
                serverVO.getJvm().getFirst().getData()).set("sysFile",
                serverVO.getSysFile().getFirst().getData());
    }
}
