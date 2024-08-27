package com.xkcoding.upload.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.xkcoding.upload.properties.AliOssProperties;
import com.xkcoding.upload.service.OssUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class OssUploadServiceImpl implements OssUploadService {

  private final AliOssProperties aliOssProperties;

  @Autowired
  public OssUploadServiceImpl(AliOssProperties aliOssProperties) {
    this.aliOssProperties = aliOssProperties;
  }

  //指定阿里云上传文件的路径
  private static final String filePre = "/fileDic/";

  @Override
  public String uploadFile(MultipartFile file, String fileName) {
    // 创建OSSClient实例。
    OSS ossClient = new OSSClientBuilder().build(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());

    try {
      // param1: Bucket名称 param2：上传到阿里云上的文件名(可以带路径)  param3: 上传文件的io流
      ossClient.putObject(aliOssProperties.getBucketName(), filePre + fileName, file.getInputStream());
    } catch (Exception oe) {
      log.error("上传文件到阿里云异常,errorMsg:{}", oe.getMessage());
      return null;
    } finally {
      /*关闭ossClient*/
      if (ossClient != null) {
        ossClient.shutdown();
      }
    }
    return aliOssProperties.getEndpoint() + filePre + fileName;
  }
}
