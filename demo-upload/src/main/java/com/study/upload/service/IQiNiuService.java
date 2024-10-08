package com.study.upload.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;

/**
 * <p>
 * 七牛云上传Service
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-06 17:21
 */
public interface IQiNiuService {
    /**
     * 七牛云上传文件
     *
     * @param file 文件
     * @return 七牛上传Response
     * @throws QiniuException 七牛异常
     */
    Response uploadFile(File file) throws QiniuException;
}
